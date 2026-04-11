import "./styles/NewSprintModal.css"
import { useState } from 'react'
import { apiFetch } from '../api'

type SprintBacklogItem = {
  id: number;
  backlogItemId: number;
  backlogItemTitle: string;
  backlogItemPriority: string;
  backlogItemEffort: number;
}

type BacklogItem = {
  id: number;
  title: string;
  priority: string;
  effort: number;
  status: string;
}

type NewSprintModalProps = {
  setIsNewSprintOpen: (value: boolean) => void;
}

function NewSprintModal({ setIsNewSprintOpen }: NewSprintModalProps){
  const [step, setStep] = useState<1 | 2>(1);
  const [name, setName] = useState("");
  const [capacity, setCapacity] = useState(0);
  const [sprintId, setSprintId] = useState<number | null>(null);
  const [proposalItems, setProposalItems] = useState<SprintBacklogItem[]>([]);
  const [availableItems, setAvailableItems] = useState<BacklogItem[]>([]);
  const [selectedAddId, setSelectedAddId] = useState<number | "">("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [submitted, setSubmitted] = useState(false);

  async function handleGenerate() {
    if (!name.trim()) { setError("Sprint name is required."); return; }
    if (capacity <= 0) { setError("Capacity must be greater than 0."); return; }

    setLoading(true);
    setError("");

    try {
      // Create the sprint
      const createRes = await apiFetch('/sprint', {
        method: 'POST',
        body: JSON.stringify({ name, capacity }),
      });

      if (!createRes.ok) {
        const msg = await createRes.text();
        setError(msg || 'Failed to create sprint.');
        return;
      }

      const sprint = await createRes.json();
      setSprintId(sprint.id);

      // Generate the proposal
      const genRes = await apiFetch(`/sprint/${sprint.id}/generate`, { method: 'POST' });
      if (!genRes.ok) {
        setError('Failed to generate proposal.');
        return;
      }
      const items: SprintBacklogItem[] = await genRes.json();
      setProposalItems(items);

      // Fetch all PLANNED backlog items for the add dropdown
      const backlogRes = await apiFetch('/backlog');
      if (backlogRes.ok) {
        const allItems: BacklogItem[] = await backlogRes.json();
        const inProposal = new Set(items.map(i => i.backlogItemId));
        setAvailableItems(allItems.filter(i => i.status === 'PLANNED' && !inProposal.has(i.id)));
      }

      setStep(2);
    } catch {
      setError('Network error. Please try again.');
    } finally {
      setLoading(false);
    }
  }

  async function handleAddItem() {
    if (!selectedAddId || !sprintId) return;
    setError("");

    const res = await apiFetch(`/sprint-backlog/${sprintId}/${selectedAddId}`, { method: 'POST' });
    if (!res.ok) {
      const msg = await res.text();
      setError(msg || 'Failed to add item.');
      return;
    }

    const added: SprintBacklogItem = await res.json();
    setProposalItems(prev => [...prev, added]);
    setAvailableItems(prev => prev.filter(i => i.id !== selectedAddId));
    setSelectedAddId("");
  }

  async function handleRemoveItem(sprintBacklogItemId: number, backlogItemId: number) {
    setError("");
    const res = await apiFetch(`/sprint-backlog/${sprintBacklogItemId}`, { method: 'DELETE' });
    if (res.status !== 204) {
      const msg = await res.text();
      setError(msg || 'Failed to remove item.');
      return;
    }

    const removed = proposalItems.find(i => i.id === sprintBacklogItemId);
    setProposalItems(prev => prev.filter(i => i.id !== sprintBacklogItemId));
    if (removed) {
      const backlogRes = await apiFetch(`/backlog/${backlogItemId}`);
      if (backlogRes.ok) {
        const item: BacklogItem = await backlogRes.json();
        setAvailableItems(prev => [...prev, item]);
      }
    }
  }

  async function handleSubmit() {
    if (!sprintId) return;
    if (proposalItems.length === 0) { setError("Add at least one item before submitting."); return; }
    setSubmitted(true);
  }

  if (submitted) {
    return (
      <div className="overlay">
        <div className="modal">
          <h1>Sprint Proposal Submitted</h1>
          <p style={{ marginTop: 16 }}>
            Your sprint proposal has been sent to the customer representative for approval.
          </p>
          <div className="new-sprint-options">
            <button id="next-new-sprint-btn" onClick={() => setIsNewSprintOpen(false)}>Done</button>
          </div>
        </div>
      </div>
    );
  }

  if (step === 1) {
    return (
      <div className="overlay">
        <div className="modal">
          <h1>Create a New Sprint</h1>
          <p className="property-prompt">Sprint Title*</p>
          <input
            id="sprint-title-input"
            value={name}
            onChange={e => setName(e.target.value)}
            placeholder="e.g., Sprint 1 - Authentication Features"
          />
          <p className="property-prompt">Team Capacity (hours)*</p>
          <input
            type="number"
            id="available-hours-input"
            value={capacity}
            onChange={e => setCapacity(Number(e.target.value))}
            placeholder="0"
            min="0"
          />
          {error && <p className="error-text" style={{ marginTop: 8 }}>{error}</p>}
          <div className="new-sprint-options">
            <button id="cancel-new-sprint-btn" onClick={() => setIsNewSprintOpen(false)}>Cancel</button>
            <button id="next-new-sprint-btn" onClick={handleGenerate} disabled={loading}>
              {loading ? 'Generating...' : 'Generate Proposal'}
            </button>
          </div>
        </div>
      </div>
    );
  }

  // Step 2 — review and modify proposal
  const totalEffort = proposalItems.reduce((sum, i) => sum + i.backlogItemEffort, 0);

  return (
    <div className="overlay">
      <div className="modal" style={{ width: 620, maxHeight: '85vh', overflowY: 'auto' }}>
        <h1>Sprint Proposal — {name}</h1>
        <p style={{ color: 'gray', marginTop: 4 }}>
          Capacity: {capacity} hrs &nbsp;|&nbsp; Used: {totalEffort} hrs &nbsp;|&nbsp; Remaining: {capacity - totalEffort} hrs
        </p>

        {proposalItems.length === 0 ? (
          <p style={{ marginTop: 16, color: 'gray' }}>No items in proposal. Add items below.</p>
        ) : (
          <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: 16 }}>
            <thead>
              <tr>
                <th style={{ textAlign: 'left', padding: '8px 4px', borderBottom: '1px solid #ccc' }}>Title</th>
                <th style={{ padding: '8px 4px', borderBottom: '1px solid #ccc' }}>Priority</th>
                <th style={{ padding: '8px 4px', borderBottom: '1px solid #ccc' }}>Effort</th>
                <th style={{ padding: '8px 4px', borderBottom: '1px solid #ccc' }}>Remove</th>
              </tr>
            </thead>
            <tbody>
              {proposalItems.map(item => (
                <tr key={item.id}>
                  <td style={{ padding: '8px 4px', borderBottom: '1px solid #eee' }}>{item.backlogItemTitle}</td>
                  <td style={{ textAlign: 'center', padding: '8px 4px', borderBottom: '1px solid #eee' }}>{item.backlogItemPriority}</td>
                  <td style={{ textAlign: 'center', padding: '8px 4px', borderBottom: '1px solid #eee' }}>{item.backlogItemEffort} hrs</td>
                  <td style={{ textAlign: 'center', padding: '8px 4px', borderBottom: '1px solid #eee' }}>
                    <button
                      onClick={() => handleRemoveItem(item.id, item.backlogItemId)}
                      style={{ background: 'none', border: 'none', color: 'red', cursor: 'pointer', fontSize: 16 }}
                    >✕</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}

        {availableItems.length > 0 && (
          <div style={{ display: 'flex', gap: 8, marginTop: 16, alignItems: 'center' }}>
            <select
              value={selectedAddId}
              onChange={e => setSelectedAddId(Number(e.target.value))}
              style={{ flex: 1, padding: '8px', fontFamily: 'Open Sans', borderRadius: 5, border: '1px solid #ccc' }}
            >
              <option value="">— Add a backlog item —</option>
              {availableItems.map(i => (
                <option key={i.id} value={i.id}>
                  [{i.priority}] {i.title} ({i.effort} hrs)
                </option>
              ))}
            </select>
            <button
              id="next-new-sprint-btn"
              onClick={handleAddItem}
              disabled={!selectedAddId}
              style={{ whiteSpace: 'nowrap' }}
            >Add Item</button>
          </div>
        )}

        {error && <p className="error-text" style={{ marginTop: 8 }}>{error}</p>}

        <div className="new-sprint-options">
          <button id="cancel-new-sprint-btn" onClick={() => setIsNewSprintOpen(false)}>Cancel</button>
          <button id="next-new-sprint-btn" onClick={handleSubmit}>Submit for Approval</button>
        </div>
      </div>
    </div>
  );
}

export default NewSprintModal;

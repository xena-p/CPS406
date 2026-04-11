import './styles/AddItemModal.css'
import { useState } from 'react';
import { apiFetch } from '../api';

type AddItemModalProps = {
  setIsNewItemOpen: (value: boolean) => void;
  onItemAdded: () => void;
}

function AddItemModal({ setIsNewItemOpen, onItemAdded }: AddItemModalProps){
  const [title, setTitle] = useState("");
  const [requirements, setRequirements] = useState("");
  const [story, setStory] = useState("");
  const [effort, setEffort] = useState(0);
  const [priority, setPriority] = useState("MEDIUM");
  const [risk, setRisk] = useState("LOW");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  async function handleConfirmAdd(){
    if (!title.trim() || !requirements.trim()) {
      setError("Title and Requirements are required.");
      return;
    }

    setLoading(true);
    setError("");

    try {
      const res = await apiFetch('/backlog', {
        method: 'POST',
        body: JSON.stringify({
          title,
          requirements,
          story,
          effort,
          priority,
          risk,
          status: 'PLANNED',
        }),
      });

      if (!res.ok) {
        const msg = await res.text();
        setError(msg || 'Failed to add item.');
        return;
      }

      onItemAdded();
      setIsNewItemOpen(false);
    } catch {
      setError('Network error. Please try again.');
    } finally {
      setLoading(false);
    }
  }

  return(
    <div className="overlay">
      <div className="modal">
        <h1 className="modal-title">Add New Backlog Item</h1>

        <p className="property-prompt">Title*</p>
        <input
          id="title-input"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          maxLength={50}
          placeholder="New Backlog Item (50 characters max)"/>

        <p className="property-prompt">Status</p>
        <button id="status-input" disabled>Planned</button>

        <p className="property-prompt">Requirements*</p>
        <textarea
          id="requirements-input"
          value={requirements}
          onChange={(e) => setRequirements(e.target.value)}
          maxLength={300}
          placeholder="Describe the requirements (300 characters max)"/>

        <p className="property-prompt">Story</p>
        <textarea
          id="story-input"
          value={story}
          onChange={(e) => setStory(e.target.value)}
          maxLength={300}
          placeholder="Describe the story (300 characters max)"/>

        <div className="dropdown-properties-div">
          <div className="priority-property">
            <p className="priority-input-text">Priority</p>
            <select
              value={priority}
              onChange={e => setPriority(e.target.value)}
              id="priority-input"
            >
              <option value="LOW">Low</option>
              <option value="MEDIUM">Medium</option>
              <option value="HIGH">High</option>
            </select>
          </div>

          <div className="estimate-property">
            <p className="estimate-input-text">Estimate (hrs)</p>
            <input
              type="number"
              value={effort}
              onChange={(e) => setEffort(Number(e.target.value))}
              id="estimate-input"
              placeholder="0"
              min="0"/>
          </div>

          <div className="risk-property">
            <p className="risk-input-text">Risk</p>
            <select
              value={risk}
              onChange={e => setRisk(e.target.value)}
              id="risk-input"
            >
              <option value="LOW">Low</option>
              <option value="MEDIUM">Medium</option>
              <option value="HIGH">High</option>
            </select>
          </div>
        </div>

        {error && <p className="error-text">{error}</p>}

        <div className="add-item-options">
          <button
            id="cancel-add-item-btn"
            onClick={() => setIsNewItemOpen(false)}
          >Cancel</button>

          <button
            id="confirm-add-item-btn"
            onClick={handleConfirmAdd}
            disabled={loading}
          >{loading ? 'Adding...' : 'Add Item'}</button>
        </div>
      </div>
    </div>
  );
}

export default AddItemModal;

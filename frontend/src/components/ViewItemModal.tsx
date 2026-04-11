import './styles/ViewItemModal.css'
import { useState } from 'react';
import { apiFetch } from '../api';

type BacklogItem = {
  id: number;
  title: string;
  requirements: string;
  story: string;
  effort: number;
  status: string;
  priority: string;
  risk: string;
}

type ViewItemModalProps = {
  setIsViewItemOpen: (value: boolean) => void;
  item: BacklogItem;
  onItemUpdated: () => void;
}

function ViewItemModal({ setIsViewItemOpen, item, onItemUpdated }: ViewItemModalProps){
  const [isEditing, setIsEditing] = useState(false);

  // Edit form state — pre-filled with current item values
  const [title, setTitle] = useState(item.title);
  const [requirements, setRequirements] = useState(item.requirements);
  const [story, setStory] = useState(item.story);
  const [effort, setEffort] = useState(item.effort);
  const [priority, setPriority] = useState(item.priority);
  const [risk, setRisk] = useState(item.risk);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  async function handleSave() {
    if (!title.trim() || !requirements.trim()) {
      setError("Title and Requirements are required.");
      return;
    }

    setLoading(true);
    setError("");

    try {
      const res = await apiFetch(`/backlog/${item.id}`, {
        method: 'PUT',
        body: JSON.stringify({
          title,
          requirements,
          story,
          effort,
          priority,
          risk,
          status: item.status,
        }),
      });

      if (!res.ok) {
        const msg = await res.text();
        setError(msg || 'Failed to update item.');
        return;
      }

      onItemUpdated();
      setIsViewItemOpen(false);
    } catch {
      setError('Network error. Please try again.');
    } finally {
      setLoading(false);
    }
  }

  if (isEditing) {
    return (
      <div className="overlay">
        <div className="view-item-modal">
          <div className="view-item-header">
            <h1 className="modal-title">Edit Backlog Item</h1>
            <button className="close-view-button" onClick={() => setIsViewItemOpen(false)}>✖</button>
          </div>

          <p className="view-property-text">Title*</p>
          <input
            value={title}
            onChange={e => setTitle(e.target.value)}
            maxLength={50}
            placeholder="Title (50 characters max)"
          />

          <p className="view-property-text">Requirements*</p>
          <textarea
            value={requirements}
            onChange={e => setRequirements(e.target.value)}
            maxLength={300}
            placeholder="Requirements (300 characters max)"
          />

          <p className="view-property-text">Story</p>
          <textarea
            value={story}
            onChange={e => setStory(e.target.value)}
            maxLength={300}
            placeholder="Story (300 characters max)"
          />

          <div className="dropdown-properties-div">
            <div className="view-dropdown-property">
              <p className="view-property-text">Priority</p>
              <select value={priority} onChange={e => setPriority(e.target.value)}>
                <option value="LOW">Low</option>
                <option value="MEDIUM">Medium</option>
                <option value="HIGH">High</option>
              </select>
            </div>

            <div className="view-dropdown-property">
              <p className="view-property-text">Estimate (hrs)</p>
              <input
                type="number"
                value={effort}
                onChange={e => setEffort(Number(e.target.value))}
                min="0"
              />
            </div>

            <div className="view-dropdown-property">
              <p className="view-property-text">Risk</p>
              <select value={risk} onChange={e => setRisk(e.target.value)}>
                <option value="LOW">Low</option>
                <option value="MEDIUM">Medium</option>
                <option value="HIGH">High</option>
              </select>
            </div>
          </div>

          {error && <p className="error-text">{error}</p>}

          <div className="add-item-options">
            <button id="cancel-add-item-btn" onClick={() => setIsEditing(false)}>Cancel</button>
            <button id="confirm-add-item-btn" onClick={handleSave} disabled={loading}>
              {loading ? 'Saving...' : 'Save Changes'}
            </button>
          </div>
        </div>
      </div>
    );
  }

  return(
    <div className="overlay">
      <div className="view-item-modal">
        <div className="view-item-header">
          <h1 className="modal-title">Backlog Item Details</h1>
          <button
            className="close-view-button"
            onClick={() => setIsViewItemOpen(false)}
          >✖</button>
        </div>

        <p className="view-property-text">Title</p>
        <p className="view-property-content">{item.title}</p>

        <p className="view-property-text">Status</p>
        <p>{item.status}</p>

        <p className="view-property-text">Requirements</p>
        <p className="view-property-content">{item.requirements}</p>

        <p className="view-property-text">Story</p>
        <p className="view-property-content">{item.story}</p>

        <div className="dropdown-properties-div">
          <div className="view-dropdown-property">
            <p className="view-property-text">Priority</p>
            <p>{item.priority}</p>
          </div>

          <div className="view-dropdown-property">
            <p className="view-property-text">Estimate</p>
            <p>{item.effort} hrs</p>
          </div>

          <div className="view-dropdown-property">
            <p className="view-property-text">Risk</p>
            <p>{item.risk}</p>
          </div>
        </div>

        {item.status === 'PLANNED' && (
          <div className="add-item-options">
            <button id="confirm-add-item-btn" onClick={() => setIsEditing(true)}>
              Edit Item
            </button>
          </div>
        )}
      </div>
    </div>
  );
}

export default ViewItemModal;

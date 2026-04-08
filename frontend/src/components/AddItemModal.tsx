import './styles/AddItemModal.css'
import { useState } from 'react';

type AddItemModalProps = {
  setIsNewItemOpen: (value: boolean) => void;
  onAddItem: (item: any) => void;
}

function AddItemModal({ setIsNewItemOpen, onAddItem }: AddItemModalProps){
  const [title, setTitle] = useState("");
  const [requirements, setRequirements] = useState("");
  const [story, setStory] = useState("");
  const [estimate, setEstimate] = useState(0);
  const [priority, setPriority] = useState("medium");
  const [risk, setRisk] = useState(0);
  const status = "Planned"

  function handleConfirmAdd(){
    const newItem = {title, status, requirements, story, priority, estimate, risk}
    onAddItem(newItem);
    setIsNewItemOpen(false);
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
        <button id="status-input">Planned</button>

        <p className="property-prompt">Requirements</p>
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
              <option value="low">Low</option>
              <option value="medium">Medium</option>
              <option value="high">High</option>
            </select>
          </div>

          <div className="estimate-property">
            <p className="estimate-input-text">Estimate</p>
            <input 
              type="number"
              value={estimate}
              onChange={(e) => setEstimate(Number(e.target.value))} 
              id="estimate-input"
              placeholder="0"
              min="0"/>
          </div>

          <div className="risk-property">
            <p className="risk-input-text">Risk</p>
            <input 
              type="number" 
              value={risk}
              onChange={(e) => setRisk(Number(e.target.value))}
              id="risk-input"
              placeholder="0"/>
          </div>
          
        </div>
        
        <div className="add-item-options">
          <button 
            id="cancel-add-item-btn"
            onClick={() => setIsNewItemOpen(false)}
          >Cancel</button>

            <button 
            id="confirm-add-item-btn"
            onClick={handleConfirmAdd}
          >Add Item</button>
        </div>
      </div>
    </div>
  );
}

export default AddItemModal;
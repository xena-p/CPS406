import './styles/AddItemModal.css'
type AddItemModalProps = {
  setIsNewItemOpen: (value: boolean) => void;
}

function AddItemModal({ setIsNewItemOpen }: AddItemModalProps){
  return(
    <div className="overlay">
      <div className="modal">
        <h1 className="modal-title">Add New Backlog Item</h1>
        <p className="property-prompt">Title*</p>
        <input id="title-input" />
        <p className="property-prompt">Status</p>
        <button id="status-input">Planned</button>
        <p className="property-prompt">Requirements</p>
        <textarea id="requirements-input"/>
        <p className="property-prompt">Story</p>
        <input id="story-input" />

        <div className="dropdown-properties-div">
          <div className="priority-property">
            <p className="priority-input-text">Priority</p>
            <button id="priority-input">High</button>
          </div>

          <div className="estimate-property">
            <p className="estimate-input-text">Estimate</p>
            <input type="number" id="estimate-input" />
          </div>

          <div className="risk-property">
            <p className="risk-input-text">Risk</p>
            <button id="risk-input">High</button>
          </div>
          
        </div>
        
        <div className="add-item-options">
          <button 
            id="cancel-add-item-btn"
            onClick={() => setIsNewItemOpen(false)}
          >Cancel</button>

            <button 
            id="confirm-add-item-btn"
            onClick={() => setIsNewItemOpen(false)}
          >Add Item</button>
        </div>
        

      </div>
    </div>
  );
}

export default AddItemModal;
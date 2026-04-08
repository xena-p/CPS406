import "./styles/NewSprintModal.css"
type NewSprintModalProps ={
  setIsNewSprintOpen: (value: boolean) => void;
}

function NewSprintModal({ setIsNewSprintOpen }: NewSprintModalProps){
  return(
    <div className="overlay">
      <div className="modal">
        <h1>Create a New Sprint</h1>
        <p className="property-prompt">Sprint Title*</p>
        <input id="sprint-title-input" placeholder="e.g., Sprint 1 - Authentication Features" />
        <p className="property-prompt">Available Hours*</p>
        <input type="number" id="available-hours-input" placeholder="0" />


        <div className="new-sprint-options">
          <button 
            id="cancel-new-sprint-btn"
            onClick={() => setIsNewSprintOpen(false)}
          >Cancel</button>

          <button 
            id="next-new-sprint-btn" 
            onClick={() => setIsNewSprintOpen(false)}
          >Next</button>
        </div>

      </div>
    </div>
  );
}

export default NewSprintModal;
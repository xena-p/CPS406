import './styles/ViewItemModal.css'
type ViewItemModalProps = {
  setIsViewItemOpen: (value: boolean) => void;
  item: any;
}

function ViewItemModal({ setIsViewItemOpen, item }: ViewItemModalProps){
  return(
    <div className="overlay">
      <div className="view-item-modal">
        <div className="view-item-header">
          <h1 className="modal-title">Backlog Item Details</h1>
          <button 
            className="close-view-button" 
            onClick={() => setIsViewItemOpen(false)}
            >
              ✖
            </button>
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
            <p>{item.estimate}</p>
          </div>

          <div className="view-dropdown-property">
            <p className="view-property-text">Risk</p>
            <p>{item.risk}</p>
          </div>
        </div>
      </div>
    </div>
  )
}

export default ViewItemModal;
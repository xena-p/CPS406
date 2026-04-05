import './styles/ProductBacklog.css'
import newSprintIcon from '../assets/new-sprint-icon.png'
import addItemIcon from '../assets/add-item-icon.png'

function ProductBacklog(){
  return(
    <div className="prod-log-div">
      <div className="prod-log-header">
        <div className="prod-log-title">
            <p className="prod-log-name">Product Backlog</p>
            <p className="prod-log-desc">All backlog items across the project</p>
        </div>

        <div className="prod-log-buttons">
          <button className="new-sprint-btn">
            <img className="new-sprint-icon" src={newSprintIcon} />
            Start a New Sprint
          </button>
          <button className="add-item-btn"> 
            <img className="add-item-icon" src={addItemIcon} />
            Add Item
          </button>
        </div>
      </div>
      <hr></hr>

      <div className="prod-log-table">
      </div>
    </div>
  );
}

export default ProductBacklog
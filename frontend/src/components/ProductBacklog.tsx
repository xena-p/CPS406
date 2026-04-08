import './styles/ProductBacklog.css'
import newSprintIcon from '../assets/new-sprint-icon.png'
import addItemIcon from '../assets/add-item-icon.png'
import { useState } from 'react'
import AddItemModal from './AddItemModal.tsx'
import NewSprintModal from "./NewSprintModal.tsx"



function ProductBacklog(){
  const [ isNewItemOpen, setIsNewItemOpen ] = useState(false);
  const [ isNewSprintOpen, setIsNewSprintOpen ] = useState(false);

  return(
    <div className="prod-log-div">
      <div className="prod-log-header">
        <div className="prod-log-title">
            <p className="prod-log-name">Product Backlog</p>
            <p className="prod-log-desc">All backlog items across the project</p>
        </div>

        <div className="prod-log-buttons">
          <button onClick={() => setIsNewSprintOpen(true)}className="new-sprint-btn">
            <img className="new-sprint-icon" src={newSprintIcon} />
            Start a New Sprint
          </button>
          <button onClick={() => setIsNewItemOpen(true)}className="add-item-btn"> 
            <img className="add-item-icon" src={addItemIcon} />
            Add Item
          </button>
        </div>
      </div>
      <hr></hr>

      <div className="prod-log-table">
      </div>

      {isNewItemOpen && (
        <AddItemModal setIsNewItemOpen={setIsNewItemOpen} />
      )}

      {isNewSprintOpen && (
        <NewSprintModal setIsNewSprintOpen={setIsNewSprintOpen} />
      )}
    </div>
  );
}

export default ProductBacklog
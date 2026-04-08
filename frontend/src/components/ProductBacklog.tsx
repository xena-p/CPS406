import './styles/ProductBacklog.css'
import newSprintIcon from '../assets/new-sprint-icon.png'
import addItemIcon from '../assets/add-item-icon.png'
import { useState, useEffect } from 'react'
import AddItemModal from './AddItemModal.tsx'
import NewSprintModal from "./NewSprintModal.tsx"

function ProductBacklog(){
  const [items, setItems] = useState<any[]>([]);
  const [ isNewItemOpen, setIsNewItemOpen ] = useState(false);
  const [ isNewSprintOpen, setIsNewSprintOpen ] = useState(false);

  useEffect(() => {
    console.log(items);
  }, [items]);

  function handleAddItems(newItem: any){
    setItems([
      ...items,
      newItem
    ])
  }

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
      

      <div className="product-backlog-table">
        <table>
          <thead>
            <tr>
              <th>Title</th>
              <th>Status</th>
              <th>Priority</th>
              <th>Estimate</th>
              <th>Actions</th>
            </tr>
          </thead>
        </table>
      </div>
     

      {isNewItemOpen && (
        <AddItemModal setIsNewItemOpen={setIsNewItemOpen} onAddItem={handleAddItems} />
      )}

      {isNewSprintOpen && (
        <NewSprintModal setIsNewSprintOpen={setIsNewSprintOpen} />
      )}
    </div>
  );
}

export default ProductBacklog
import './styles/ProductBacklog.css'
import newSprintIcon from '../assets/new-sprint-icon.png'
import addItemIcon from '../assets/add-item-icon.png'
import viewItemIcon from '../assets/view-item-icon.png'
import deleteItemIcon from '../assets/delete-item-icon.png'
import { useState, useEffect } from 'react'
import AddItemModal from './AddItemModal.tsx'
import NewSprintModal from "./NewSprintModal.tsx"
import ViewItemModal from './ViewItemModal.tsx'

function ProductBacklog(){
  const [items, setItems] = useState<any[]>([]);
  const [ isNewItemOpen, setIsNewItemOpen ] = useState(false);
  const [ isNewSprintOpen, setIsNewSprintOpen ] = useState(false);
  const [ isViewItemOpen, setIsViewItemOpen ] = useState(false);
  const [ selectedItem, setSelectedItem ] = useState<any>(null);

  useEffect(() => {
    console.log(items);
  }, [items]);

  function handleAddItem(newItem: any){
    setItems([
      ...items,
      newItem
    ])
  }

  function handleDeleteItem(id: string){
    setItems(items.filter(item => item.id !== id))
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
          <tbody>
            {items.length === 0 ? (
              <tr>
                <td colSpan={5}>
                  <p>No items in the product backlog.</p>
                </td>
              </tr>
            ) : (
              items.map((item) => (
                <tr key={item.id}>
                  <td className="item-title-cell">{item.title}</td>
                  <td>{item.status}</td>
                  <td>{item.priority}</td>
                  <td>{item.estimate} hrs</td>
                  <td>
                    <div className="actions-div">
                      <button 
                        id="view-item-btn"
                        onClick={() => { setSelectedItem(item); setIsViewItemOpen(true); }}
                      >
                        <img id="view-item-icon" src={viewItemIcon}></img>
                      </button>
                      <button 
                        id="delete-item-btn"
                        onClick={() => handleDeleteItem(item.id)}
                      >
                        <img id="delete-item-icon" src={deleteItemIcon}></img>
                      </button>
                    </div>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {isNewItemOpen && (
        <AddItemModal setIsNewItemOpen={setIsNewItemOpen} onAddItem={handleAddItem} />
      )}

      {isNewSprintOpen && (
        <NewSprintModal setIsNewSprintOpen={setIsNewSprintOpen} />
      )}

      {isViewItemOpen && (
        <ViewItemModal setIsViewItemOpen={setIsViewItemOpen} item={selectedItem}/>
      )}
    </div>
  );
}

export default ProductBacklog
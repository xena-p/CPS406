import './styles/ProductBacklog.css'
import newSprintIcon from '../assets/new-sprint-icon.png'
import addItemIcon from '../assets/add-item-icon.png'
import viewItemIcon from '../assets/view-item-icon.png'
import deleteItemIcon from '../assets/delete-item-icon.png'
import { useState, useEffect, useCallback } from 'react'
import AddItemModal from './AddItemModal.tsx'
import NewSprintModal from "./NewSprintModal.tsx"
import ViewItemModal from './ViewItemModal.tsx'
import { apiFetch } from '../api'

type BacklogItem = {
  id: number;
  title: string;
  requirements: string;
  story: string;
  effort: number;
  createdById: number | null;
  createdAt: string;
  updatedAt: string;
  status: string;
  priority: string;
  risk: string;
}

const STATUS_ORDER: Record<string, number> = { IN_PROGRESS: 0, ACTIVE: 1, PLANNED: 2, DONE: 3 };
const PRIORITY_ORDER: Record<string, number> = { HIGH: 0, MEDIUM: 1, LOW: 2 };

function sortItems(items: BacklogItem[]): BacklogItem[] {
  return [...items].sort((a, b) => {
    const sd = (STATUS_ORDER[a.status] ?? 99) - (STATUS_ORDER[b.status] ?? 99);
    if (sd !== 0) return sd;
    return (PRIORITY_ORDER[a.priority] ?? 99) - (PRIORITY_ORDER[b.priority] ?? 99);
  });
}

function ProductBacklog(){
  const [items, setItems] = useState<BacklogItem[]>([]);
  const [isNewItemOpen, setIsNewItemOpen] = useState(false);
  const [isNewSprintOpen, setIsNewSprintOpen] = useState(false);
  const [isViewItemOpen, setIsViewItemOpen] = useState(false);
  const [selectedItem, setSelectedItem] = useState<BacklogItem | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const fetchItems = useCallback(async () => {
    setLoading(true);
    setError("");
    try {
      const res = await apiFetch('/backlog');
      if (!res.ok) throw new Error('Failed to load backlog');
      const data: BacklogItem[] = await res.json();
      setItems(sortItems(data));
    } catch {
      setError('Could not load backlog items.');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchItems();
  }, [fetchItems]);

  async function handleDeleteItem(id: number) {
    const res = await apiFetch(`/backlog/${id}`, { method: 'DELETE' });
    if (res.status === 204) {
      setItems(prev => prev.filter(item => item.id !== id));
    } else {
      const msg = await res.text();
      alert(msg || 'Could not delete item.');
    }
  }

  return(
    <div className="prod-log-div">
      <div className="prod-log-header">
        <div className="prod-log-title">
          <p className="prod-log-name">Product Backlog</p>
          <p className="prod-log-desc">All backlog items across the project</p>
        </div>

        <div className="prod-log-buttons">
          <button onClick={() => setIsNewSprintOpen(true)} className="new-sprint-btn">
            <img className="new-sprint-icon" src={newSprintIcon} />
            Start a New Sprint
          </button>
          <button onClick={() => setIsNewItemOpen(true)} className="add-item-btn">
            <img className="add-item-icon" src={addItemIcon} />
            Add Item
          </button>
        </div>
      </div>

      <div className="product-backlog-table">
        {loading ? (
          <p>Loading...</p>
        ) : error ? (
          <p>{error}</p>
        ) : (
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
                    <td>{item.effort} hrs</td>
                    <td>
                      <div className="actions-div">
                        <button
                          id="view-item-btn"
                          onClick={() => { setSelectedItem(item); setIsViewItemOpen(true); }}
                        >
                          <img id="view-item-icon" src={viewItemIcon} />
                        </button>
                        {item.status === 'PLANNED' && (
                          <button
                            id="delete-item-btn"
                            onClick={() => handleDeleteItem(item.id)}
                          >
                            <img id="delete-item-icon" src={deleteItemIcon} />
                          </button>
                        )}
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        )}
      </div>

      {isNewItemOpen && (
        <AddItemModal
          setIsNewItemOpen={setIsNewItemOpen}
          onItemAdded={fetchItems}
        />
      )}

      {isNewSprintOpen && (
        <NewSprintModal setIsNewSprintOpen={setIsNewSprintOpen} />
      )}

      {isViewItemOpen && selectedItem && (
        <ViewItemModal
          setIsViewItemOpen={setIsViewItemOpen}
          item={selectedItem}
          onItemUpdated={fetchItems}
        />
      )}
    </div>
  );
}

export default ProductBacklog

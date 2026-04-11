import { useState, useEffect } from 'react'
import './styles/SprintView.css'
import NewSprintModal from './NewSprintModal'
import { apiFetch } from '../api'

type Sprint = {
  id: number;
  name: string;
  status: string;
  capacity: number;
  approved: boolean;
  startDate: string | null;
  endDate: string | null;
}

type SprintBacklogItem = {
  id: number;
  backlogItemId: number;
  backlogItemTitle: string;
  backlogItemPriority: string;
  backlogItemEffort: number;
  status: string;
  locked: boolean;
}

type EngineeringTask = {
  id: number;
  sprintBacklogItemId: number;
  title: string;
  description: string;
  effort: number;
  status: string;
}

const STATUS_LABEL: Record<string, string> = {
  PLANNED: 'Pending Approval',
  ACTIVE: 'Active',
  DONE: 'Completed',
  IN_PROGRESS: 'In Progress',
}

const STATUS_COLOR: Record<string, string> = {
  PLANNED: '#f59e0b',
  ACTIVE: '#10b981',
  DONE: '#6b7280',
  IN_PROGRESS: '#3b82f6',
}

function SprintView() {
  const [sprints, setSprints] = useState<Sprint[]>([]);
  const [selectedSprint, setSelectedSprint] = useState<Sprint | null>(null);
  const [sprintItems, setSprintItems] = useState<SprintBacklogItem[]>([]);
  const [isNewSprintOpen, setIsNewSprintOpen] = useState(false);
  const [loading, setLoading] = useState(true);
  const [itemsLoading, setItemsLoading] = useState(false);
  const [completing, setCompleting] = useState(false);

  // Task management state
  const [expandedItemId, setExpandedItemId] = useState<number | null>(null);
  const [tasks, setTasks] = useState<Record<number, EngineeringTask[]>>({});
  const [tasksLoading, setTasksLoading] = useState(false);
  const [newTaskTitle, setNewTaskTitle] = useState('');
  const [newTaskDesc, setNewTaskDesc] = useState('');
  const [newTaskEffort, setNewTaskEffort] = useState(0);
  const [taskError, setTaskError] = useState('');

  useEffect(() => { fetchSprints(); }, []);

  async function fetchSprints() {
    setLoading(true);
    try {
      const res = await apiFetch('/sprint');
      if (res.ok) {
        const data: Sprint[] = await res.json();
        const order: Record<string, number> = { ACTIVE: 0, IN_PROGRESS: 1, PLANNED: 2, DONE: 3 };
        data.sort((a, b) => (order[a.status] ?? 99) - (order[b.status] ?? 99));
        setSprints(data);
      }
    } finally {
      setLoading(false);
    }
  }

  async function handleSelectSprint(sprint: Sprint) {
    setSelectedSprint(sprint);
    setExpandedItemId(null);
    setItemsLoading(true);
    try {
      const res = await apiFetch(`/sprint-backlog/${sprint.id}`);
      if (res.ok) setSprintItems(await res.json());
    } finally {
      setItemsLoading(false);
    }
  }

  function handleModalClose(value: boolean) {
    setIsNewSprintOpen(value);
    if (!value) fetchSprints();
  }

  async function handleCompleteSprint() {
    if (!selectedSprint) return;
    setCompleting(true);
    try {
      const res = await apiFetch(`/sprint/${selectedSprint.id}/complete`, { method: 'PUT' });
      if (res.ok) {
        await fetchSprints();
        setSelectedSprint(prev => prev ? { ...prev, status: 'DONE' } : null);
      }
    } finally {
      setCompleting(false);
    }
  }

  async function handleMarkItemDone(itemId: number) {
    const res = await apiFetch(`/sprint-backlog/${itemId}/done`, { method: 'PUT' });
    if (res.ok) {
      const updated: SprintBacklogItem = await res.json();
      setSprintItems(prev => prev.map(i => i.id === itemId ? updated : i));
    }
  }

  async function handleToggleTasks(itemId: number) {
    if (expandedItemId === itemId) {
      setExpandedItemId(null);
      return;
    }
    setExpandedItemId(itemId);
    setTaskError('');
    setNewTaskTitle(''); setNewTaskDesc(''); setNewTaskEffort(0);
    if (!tasks[itemId]) {
      setTasksLoading(true);
      try {
        const res = await apiFetch(`/tasks/item/${itemId}`);
        
        if (res.ok) {
        const data = await res.json(); 
        
        setTasks(prev => ({ ...prev, [itemId]: data }));
      }
    } catch (err) {
      setTaskError('Failed to load tasks');
    } finally {
      setTasksLoading(false);
      }
    }
  }

  async function handleAddTask(itemId: number) {
    if (!newTaskTitle.trim()) { setTaskError('Title is required.'); return; }
    setTaskError('');
    const res = await apiFetch(`/tasks/item/${itemId}`, {
      method: 'POST',
      body: JSON.stringify({ title: newTaskTitle, description: newTaskDesc, effort: newTaskEffort }),
    });
    if (res.ok) {
      const created: EngineeringTask = await res.json();
      setTasks(prev => ({ ...prev, [itemId]: [...(prev[itemId] ?? []), created] }));
      setNewTaskTitle(''); setNewTaskDesc(''); setNewTaskEffort(0);
    } else {
      setTaskError('Failed to add task.');
    }
  }

  async function handleMarkTaskDone(itemId: number, task: EngineeringTask) {
    const res = await apiFetch(`/tasks/${task.id}`, {
      method: 'PUT',
      body: JSON.stringify({ ...task, status: 'DONE' }),
    });
    if (res.ok) {
      const updated: EngineeringTask = await res.json();
      setTasks(prev => ({ ...prev, [itemId]: prev[itemId].map(t => t.id === task.id ? updated : t) }));
    }
  }

  async function handleDeleteTask(itemId: number, taskId: number) {
    const res = await apiFetch(`/tasks/${taskId}`, { method: 'DELETE' });
    if (res.status === 204) {
      setTasks(prev => ({ ...prev, [itemId]: prev[itemId].filter(t => t.id !== taskId) }));
    }
  }

  const totalEffort = sprintItems.reduce((sum, i) => sum + i.backlogItemEffort, 0);
  const doneCount = sprintItems.filter(i => i.status === 'DONE').length;
  const isActive = selectedSprint?.status === 'ACTIVE';

  return (
    <div className="sprint-view">
      <div className="sprint-list-panel">
        <div className="sprint-list-header">
          <h2>Sprints</h2>
          <button className="new-sprint-btn" onClick={() => setIsNewSprintOpen(true)}>+ New Sprint</button>
        </div>

        {loading ? (
          <p className="sprint-loading">Loading sprints...</p>
        ) : sprints.length === 0 ? (
          <p className="sprint-empty">No sprints yet. Create one to get started.</p>
        ) : (
          <div className="sprint-cards">
            {sprints.map(sprint => (
              <div
                key={sprint.id}
                className={`sprint-card ${selectedSprint?.id === sprint.id ? 'sprint-card-active' : ''}`}
                onClick={() => handleSelectSprint(sprint)}
              >
                <div className="sprint-card-name">{sprint.name}</div>
                <div className="sprint-card-meta">
                  <span className="sprint-status-badge" style={{ backgroundColor: STATUS_COLOR[sprint.status] ?? '#6b7280' }}>
                    {STATUS_LABEL[sprint.status] ?? sprint.status}
                  </span>
                  <span className="sprint-capacity">{sprint.capacity} hrs capacity</span>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      <div className="sprint-detail-panel">
        {!selectedSprint ? (
          <div className="sprint-detail-empty"><p>Select a sprint to view its backlog items.</p></div>
        ) : (
          <>
            <div className="sprint-detail-header">
              <div>
                <h2>{selectedSprint.name}</h2>
                <p className="sprint-detail-meta">
                  <span className="sprint-status-badge" style={{ backgroundColor: STATUS_COLOR[selectedSprint.status] ?? '#6b7280' }}>
                    {STATUS_LABEL[selectedSprint.status] ?? selectedSprint.status}
                  </span>
                  &nbsp;&nbsp;Capacity: {selectedSprint.capacity} hrs
                  &nbsp;|&nbsp; Used: {totalEffort} hrs
                  &nbsp;|&nbsp; Remaining: {selectedSprint.capacity - totalEffort} hrs
                  {isActive && sprintItems.length > 0 && (
                    <>&nbsp;|&nbsp; Progress: {doneCount}/{sprintItems.length} done</>
                  )}
                </p>
              </div>
              {isActive && (
                <button
                  onClick={handleCompleteSprint}
                  disabled={completing}
                  style={{
                    background: '#1f2937', color: 'white', border: 'none',
                    borderRadius: 8, padding: '10px 18px', fontFamily: 'Open Sans',
                    fontWeight: 600, fontSize: 14, cursor: 'pointer', flexShrink: 0
                  }}
                >
                  {completing ? 'Completing...' : 'Complete Sprint'}
                </button>
              )}
            </div>

            {itemsLoading ? (
              <p className="sprint-loading">Loading items...</p>
            ) : sprintItems.length === 0 ? (
              <p className="sprint-empty">No backlog items in this sprint.</p>
            ) : (
              <table className="sprint-items-table">
                <thead>
                  <tr>
                    <th>Title</th>
                    <th>Priority</th>
                    <th>Effort</th>
                    <th>Status</th>
                    {isActive && <th>Actions</th>}
                  </tr>
                </thead>
                <tbody>
                  {sprintItems.map(item => (
                    <>
                      <tr
                        key={item.id}
                        className={expandedItemId === item.id ? 'sprint-item-row-expanded' : 'sprint-item-row'}
                        onClick={() => isActive && handleToggleTasks(item.id)}
                        style={{ cursor: isActive ? 'pointer' : 'default' }}
                      >
                        <td>
                          {isActive && (
                            <span style={{ marginRight: 6, fontSize: 11, color: '#9ca3af' }}>
                              {expandedItemId === item.id ? '▼' : '▶'}
                            </span>
                          )}
                          {item.backlogItemTitle}
                        </td>
                        <td className="cell-center">{item.backlogItemPriority}</td>
                        <td className="cell-center">{item.backlogItemEffort} hrs</td>
                        <td className="cell-center">
                          <span className={`item-status item-status-${item.status.toLowerCase()}`}>
                            {item.status}
                          </span>
                        </td>
                        {isActive && (
                          <td className="cell-center" onClick={e => e.stopPropagation()}>
                            {item.status !== 'DONE' && (
                              <button
                                onClick={() => handleMarkItemDone(item.id)}
                                style={{
                                  background: '#10b981', color: 'white', border: 'none',
                                  borderRadius: 6, padding: '4px 10px', fontSize: 12,
                                  fontWeight: 600, cursor: 'pointer', fontFamily: 'Open Sans'
                                }}
                              >Mark Done</button>
                            )}
                          </td>
                        )}
                      </tr>

                      {isActive && expandedItemId === item.id && (
                        <tr key={`tasks-${item.id}`}>
                          <td colSpan={5} style={{ padding: 0, background: '#f9fafb' }}>
                            <div className="task-panel">
                              <p className="task-panel-title">Engineering Tasks for: <strong>{item.backlogItemTitle}</strong></p>

                              {tasksLoading ? (
                                <p className="sprint-loading">Loading tasks...</p>
                              ) : (tasks[item.id] ?? []).length === 0 ? (
                                <p className="sprint-empty">No tasks yet. Add one below.</p>
                              ) : (
                                <table className="task-table">
                                  <thead>
                                    <tr>
                                      <th>Title</th>
                                      <th>Description</th>
                                      <th>Effort</th>
                                      <th>Status</th>
                                      <th>Actions</th>
                                    </tr>
                                  </thead>
                                  <tbody>
                                    {(tasks[item.id] ?? []).map(task => (
                                      <tr key={task.id}>
                                        <td>{task.title}</td>
                                        <td style={{ color: '#6b7280', fontSize: 13 }}>{task.description}</td>
                                        <td className="cell-center">{task.effort} hrs</td>
                                        <td className="cell-center">
                                          <span className={`item-status item-status-${task.status.toLowerCase()}`}>
                                            {task.status}
                                          </span>
                                        </td>
                                        <td className="cell-center">
                                          <div style={{ display: 'flex', gap: 6, justifyContent: 'center' }}>
                                            {task.status !== 'DONE' && (
                                              <button
                                                onClick={() => handleMarkTaskDone(item.id, task)}
                                                className="task-done-btn"
                                              >Done</button>
                                            )}
                                            <button
                                              onClick={() => handleDeleteTask(item.id, task.id)}
                                              className="task-delete-btn"
                                            >✕</button>
                                          </div>
                                        </td>
                                      </tr>
                                    ))}
                                  </tbody>
                                </table>
                              )}

                              <div className="task-add-form">
                                <input
                                  placeholder="Task title*"
                                  value={newTaskTitle}
                                  onChange={e => setNewTaskTitle(e.target.value)}
                                  className="task-input"
                                />
                                <input
                                  placeholder="Description"
                                  value={newTaskDesc}
                                  onChange={e => setNewTaskDesc(e.target.value)}
                                  className="task-input task-input-wide"
                                />
                                <input
                                  type="number"
                                  placeholder="Hrs"
                                  value={newTaskEffort}
                                  onChange={e => setNewTaskEffort(Number(e.target.value))}
                                  className="task-input task-input-small"
                                  min="0"
                                />
                                <button onClick={() => handleAddTask(item.id)} className="task-add-btn">
                                  + Add Task
                                </button>
                              </div>
                              {taskError && <p style={{ color: '#ef4444', fontSize: 13, margin: '4px 0 0' }}>{taskError}</p>}
                            </div>
                          </td>
                        </tr>
                      )}
                    </>
                  ))}
                </tbody>
              </table>
            )}
          </>
        )}
      </div>

      {isNewSprintOpen && <NewSprintModal setIsNewSprintOpen={handleModalClose} />}
    </div>
  );
}

export default SprintView

import { useState, useEffect } from 'react'
import './styles/InboxView.css'
import { apiFetch } from '../api'
import { useAuth } from '../AuthContext'

type Sprint = {
  id: number;
  name: string;
  status: string;
  capacity: number;
  approved: boolean;
}

type SprintBacklogItem = {
  id: number;
  backlogItemTitle: string;
  backlogItemPriority: string;
  backlogItemEffort: number;
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

function InboxView() {
  const { user } = useAuth();
  const isRep = user?.role === 'REPRESENTATIVE';

  const [sprints, setSprints] = useState<Sprint[]>([]);
  const [selectedSprint, setSelectedSprint] = useState<Sprint | null>(null);
  const [sprintItems, setSprintItems] = useState<SprintBacklogItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [itemsLoading, setItemsLoading] = useState(false);
  const [actionLoading, setActionLoading] = useState(false);
  const [message, setMessage] = useState('');

  useEffect(() => {
    fetchSprints();
  }, []);

  async function fetchSprints() {
    setLoading(true);
    try {
      const res = await apiFetch('/sprint');
      if (res.ok) {
        const data: Sprint[] = await res.json();
        // REPRESENTATIVE: only show PLANNED (pending approval)
        // DEVELOPER: show all sprints as a status view
        const filtered = isRep ? data.filter(s => s.status === 'PLANNED') : data;
        const order: Record<string, number> = { PLANNED: 0, ACTIVE: 1, IN_PROGRESS: 2, DONE: 3 };
        filtered.sort((a, b) => (order[a.status] ?? 99) - (order[b.status] ?? 99));
        setSprints(filtered);
      }
    } finally {
      setLoading(false);
    }
  }

  async function handleSelectSprint(sprint: Sprint) {
    setSelectedSprint(sprint);
    setMessage('');
    setItemsLoading(true);
    try {
      const res = await apiFetch(`/sprint-backlog/${sprint.id}`);
      if (res.ok) {
        setSprintItems(await res.json());
      }
    } finally {
      setItemsLoading(false);
    }
  }

  async function handleProcess(approved: boolean) {
    if (!selectedSprint) return;
    setActionLoading(true);
    setMessage('');
    try {
      const res = await apiFetch(
        `/sprint/${selectedSprint.id}/process?approved=${approved}`,
        { method: 'PUT' }
      );
      if (res.ok) {
        setMessage(approved
          ? 'Sprint approved! It is now active.'
          : 'Sprint proposal rejected and removed.');
        setSelectedSprint(null);
        setSprintItems([]);
        await fetchSprints();
      } else {
        const txt = await res.text();
        setMessage(txt || 'Action failed.');
      }
    } finally {
      setActionLoading(false);
    }
  }

  const totalEffort = sprintItems.reduce((sum, i) => sum + i.backlogItemEffort, 0);

  return (
    <div className="inbox-view">
      <div className="inbox-list-panel">
        <div className="inbox-list-header">
          <h2>{isRep ? 'Pending Proposals' : 'Sprint Status'}</h2>
        </div>

        {loading ? (
          <p className="inbox-loading">Loading...</p>
        ) : sprints.length === 0 ? (
          <p className="inbox-empty">
            {isRep ? 'No sprint proposals pending approval.' : 'No sprints to display.'}
          </p>
        ) : (
          <div className="inbox-cards">
            {sprints.map(sprint => (
              <div
                key={sprint.id}
                className={`inbox-card ${selectedSprint?.id === sprint.id ? 'inbox-card-active' : ''}`}
                onClick={() => handleSelectSprint(sprint)}
              >
                <div className="inbox-card-name">{sprint.name}</div>
                <div className="inbox-card-meta">
                  <span
                    className="inbox-status-badge"
                    style={{ backgroundColor: STATUS_COLOR[sprint.status] ?? '#6b7280' }}
                  >
                    {STATUS_LABEL[sprint.status] ?? sprint.status}
                  </span>
                  <span className="inbox-capacity">{sprint.capacity} hrs</span>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      <div className="inbox-detail-panel">
        {message && (
          <div className={`inbox-message ${message.includes('rejected') || message.includes('failed') ? 'inbox-message-error' : 'inbox-message-success'}`}>
            {message}
          </div>
        )}

        {!selectedSprint ? (
          <div className="inbox-detail-empty">
            <p>{isRep ? 'Select a proposal to review and approve or reject it.' : 'Select a sprint to view its items.'}</p>
          </div>
        ) : (
          <>
            <div className="inbox-detail-header">
              <div>
                <h2>{selectedSprint.name}</h2>
                <p className="inbox-detail-meta">
                  <span
                    className="inbox-status-badge"
                    style={{ backgroundColor: STATUS_COLOR[selectedSprint.status] ?? '#6b7280' }}
                  >
                    {STATUS_LABEL[selectedSprint.status] ?? selectedSprint.status}
                  </span>
                  &nbsp;&nbsp;Capacity: {selectedSprint.capacity} hrs
                  &nbsp;|&nbsp; Total effort: {totalEffort} hrs
                </p>
              </div>

              {isRep && selectedSprint.status === 'PLANNED' && (
                <div className="inbox-actions">
                  <button
                    className="inbox-reject-btn"
                    onClick={() => handleProcess(false)}
                    disabled={actionLoading}
                  >
                    Reject
                  </button>
                  <button
                    className="inbox-approve-btn"
                    onClick={() => handleProcess(true)}
                    disabled={actionLoading}
                  >
                    {actionLoading ? 'Processing...' : 'Approve'}
                  </button>
                </div>
              )}
            </div>

            {itemsLoading ? (
              <p className="inbox-loading">Loading items...</p>
            ) : sprintItems.length === 0 ? (
              <p className="inbox-empty">No items in this sprint.</p>
            ) : (
              <table className="inbox-items-table">
                <thead>
                  <tr>
                    <th>Title</th>
                    <th>Priority</th>
                    <th>Effort</th>
                    <th>Status</th>
                  </tr>
                </thead>
                <tbody>
                  {sprintItems.map(item => (
                    <tr key={item.id}>
                      <td>{item.backlogItemTitle}</td>
                      <td className="cell-center">{item.backlogItemPriority}</td>
                      <td className="cell-center">{item.backlogItemEffort} hrs</td>
                      <td className="cell-center">
                        <span className={`item-status item-status-${item.status.toLowerCase()}`}>
                          {item.status}
                        </span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </>
        )}
      </div>
    </div>
  );
}

export default InboxView

import { useState, useEffect } from 'react'
import './styles/ReportsView.css'
import { apiFetch } from '../api'

type Sprint = {
  id: number;
  name: string;
  status: string;
}

type ReportDto = {
  sprintId: number;
  sprintName: string;
  totalItems: number;
  completedItems: number;
  totalPlannedEffort: number;
  totalActualEffort: number;
  burndownData: { day: number; ideal: number; actual?: number }[];
}

function ReportsView() {
  const [sprints, setSprints] = useState<Sprint[]>([]);
  const [selectedSprintId, setSelectedSprintId] = useState<number | ''>('');
  const [report, setReport] = useState<ReportDto | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    apiFetch('/sprint').then(res => {
      if (res.ok) res.json().then(setSprints);
    });
  }, []);

  async function handleSelectSprint(id: number) {
    setSelectedSprintId(id);
    setError('');
    setLoading(true);
    try {
      const res = await apiFetch(`/report/${id}`);
      if (res.ok) {
        setReport(await res.json());
      } else {
        setError('Failed to load report.');
        setReport(null);
      }
    } catch {
      setError('Network error.');
    } finally {
      setLoading(false);
    }
  }

  const completionPct = report && report.totalItems > 0
    ? Math.round((report.completedItems / report.totalItems) * 100)
    : 0;

  const effortPct = report && report.totalPlannedEffort > 0
    ? Math.round((report.completedItems / report.totalItems) * 100)
    : 0;

  return (
    <div className="reports-view">
      <div className="reports-header">
        <h2>Sprint Reports</h2>
        <select
          value={selectedSprintId}
          onChange={e => handleSelectSprint(Number(e.target.value))}
          className="reports-sprint-select"
        >
          <option value="">— Select a sprint —</option>
          {sprints.map(s => (
            <option key={s.id} value={s.id}>{s.name} ({s.status})</option>
          ))}
        </select>
      </div>

      {loading && <p className="reports-loading">Loading report...</p>}
      {error && <p className="reports-error">{error}</p>}

      {!loading && !report && !error && (
        <div className="reports-empty">
          <p>Select a sprint above to view its report.</p>
        </div>
      )}

      {report && !loading && (
        <div className="reports-content">
          <h3 className="reports-sprint-name">{report.sprintName}</h3>

          {/* Stat cards */}
          <div className="stat-cards">
            <div className="stat-card">
              <p className="stat-label">Items Completed</p>
              <p className="stat-value">{report.completedItems} / {report.totalItems}</p>
              <div className="stat-bar">
                <div className="stat-bar-fill stat-bar-green" style={{ width: `${completionPct}%` }} />
              </div>
              <p className="stat-pct">{completionPct}%</p>
            </div>

            <div className="stat-card">
              <p className="stat-label">Planned Effort</p>
              <p className="stat-value">{report.totalPlannedEffort} hrs</p>
            </div>

            <div className="stat-card">
              <p className="stat-label">Actual Effort Logged</p>
              <p className="stat-value">{report.totalActualEffort} hrs</p>
            </div>

            <div className="stat-card">
              <p className="stat-label">Remaining Effort</p>
              <p className="stat-value">
                {report.totalPlannedEffort - report.burndownData
                  .filter(d => d.actual !== undefined)
                  .reduce((_, d) => d.actual ?? 0, 0)} hrs
              </p>
            </div>
          </div>

          {/* Burndown table */}
          <div className="burndown-section">
            <h4>Burndown Chart (Ideal vs. Actual)</h4>
            <div className="burndown-chart">
              {report.burndownData.map(point => {
                const maxEffort = report.totalPlannedEffort || 1;
                const idealHeight = Math.round((point.ideal / maxEffort) * 100);
                const actualHeight = point.actual !== undefined
                  ? Math.round((point.actual / maxEffort) * 100)
                  : null;
                return (
                  <div key={point.day} className="burndown-col">
                    <div className="burndown-bars">
                      <div
                        className="burndown-bar-ideal"
                        style={{ height: `${idealHeight}%` }}
                        title={`Day ${point.day} ideal: ${point.ideal} hrs`}
                      />
                      {actualHeight !== null && (
                        <div
                          className="burndown-bar-actual"
                          style={{ height: `${actualHeight}%` }}
                          title={`Day ${point.day} actual: ${point.actual} hrs`}
                        />
                      )}
                    </div>
                    <p className="burndown-day">D{point.day}</p>
                  </div>
                );
              })}
            </div>
            <div className="burndown-legend">
              <span className="legend-ideal" /> Ideal &nbsp;&nbsp;
              <span className="legend-actual" /> Actual
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default ReportsView

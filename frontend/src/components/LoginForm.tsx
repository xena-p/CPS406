import './styles/LoginForm.css'
import { useState } from 'react';
import { useAuth } from '../AuthContext';

function LoginForm(){
  const { login } = useAuth();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleLogin = async () => {
    setError("");
    setLoading(true);
    try {
      await login(email, password);
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'Login failed.');
    } finally {
      setLoading(false);
    }
  };

  return(
    <div className="login-page">
      <div className="login-popup">
        <h1>Scrum Project Manager</h1>
        <h2>Sign in to your account to continue</h2>

        <p className="input-prompt">Email</p>
        <input
          type="email"
          className="email-input"
          placeholder="user@example.com"
          onChange={(e) => setEmail(e.target.value)}
        />

        <p className="input-prompt">Password</p>
        <input
          type="password"
          className="password-input"
          placeholder="Enter your password"
          onChange={(e) => setPassword(e.target.value)}
          onKeyDown={(e) => e.key === 'Enter' && handleLogin()}
        />

        {error && <p className="error-text">{error}</p>}

        <div
          className="login-btn-div"
          onClick={handleLogin}
        >
          <button className="login-btn" disabled={loading}>
            {loading ? 'Signing in...' : 'Log In'}
          </button>
        </div>
      </div>
    </div>
  );
}

export default LoginForm

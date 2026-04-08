import './styles/LoginForm.css'
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';

function LoginForm(){
  const navigate = useNavigate();
  const [ email, setEmail ] = useState("");
  const [ password, setPassword ] = useState("");

  const handleLogin = () => {
    if (email === 'dev@example.com' && password === 'password'){
      navigate('/dashboard');
    } else {
      navigate('/dashboard');
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
        />

        <div 
          className="login-btn-div"
          onClick={handleLogin}
        >
          <button className="login-btn">Log In</button>
        </div>
      </div>
    </div>
  );
}

export default LoginForm
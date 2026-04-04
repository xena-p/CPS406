import './LoginForm.css'
function LoginForm(){
  return(
    <div className="login-popup">
      <h1>Scrum Project Manager</h1>
      <h2>Sign in to your account to continue</h2>

      <p>Email</p>
      <input type="email" className="email-input" placeholder="user@example.com" />
      
      <p>Password</p>
      <input type="password" className="password" placeholder="Enter your password" />

      <div className="signup-btn-div">
        <button className="login-btn">Log In</button>
      </div>
    </div>
  );
}

export default LoginForm
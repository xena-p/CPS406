import { createContext, useContext, useState, ReactNode } from 'react';
import { useNavigate } from 'react-router-dom';

interface User {
  email: string;
  role: string;
  accessToken: string;
}

interface AuthContextType {
  user: User | null;
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | null>(null);

// Decodes the payload of a JWT (base64) to read claims — no secret needed for reading
function decodeJwtPayload(token: string): { sub: string; role: string } {
  const payload = token.split('.')[1];
  return JSON.parse(atob(payload));
}

function loadUserFromStorage(): User | null {
  const token = localStorage.getItem('accessToken');
  if (!token) return null;
  try {
    const { sub, role } = decodeJwtPayload(token);
    return { email: sub, role, accessToken: token };
  } catch {
    return null;
  }
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(loadUserFromStorage);
  const navigate = useNavigate();

  const login = async (email: string, password: string) => {
    const res = await fetch('http://localhost:8080/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password }),
    });

    if (!res.ok) {
      // 401 = bad credentials or locked account
      throw new Error(res.status === 401 ? 'Invalid email or password.' : 'Login failed. Please try again.');
    }

    const data = await res.json();
    const { sub, role } = decodeJwtPayload(data.accessToken);

    localStorage.setItem('accessToken', data.accessToken);
    localStorage.setItem('refreshToken', data.refreshToken);

    setUser({ email: sub, role, accessToken: data.accessToken });
    navigate('/dashboard');
  };

  const logout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    setUser(null);
    navigate('/');
  };

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth(): AuthContextType {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used inside AuthProvider');
  return ctx;
}

import React, { createContext, useContext, useEffect, useState } from "react";
import { getAuth, onAuthStateChanged, type User } from "firebase/auth";
import { app } from "../../auth/firebase";

interface AuthContextProps {
  user: User | null;
  loading: boolean;
  backendUserId: string | null;
  setBackendUserId: (id: string | null) => void;
}

const AuthContext = createContext<AuthContextProps>({
  user: null,
  loading: true,
  backendUserId: null,
  setBackendUserId: () => {},
});

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);
  const [backendUserId, setBackendUserId] = useState<string | null>(null);

  useEffect(() => {
    const auth = getAuth(app);
    const unsubscribe = onAuthStateChanged(auth, (firebaseUser) => {
      setUser(firebaseUser);
      setLoading(false);
    });

    return () => unsubscribe();
  }, []);

  return (
    <AuthContext.Provider value={{ user, loading, backendUserId, setBackendUserId }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);

import { useState, type FormEvent } from 'react';

function App() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    console.log('Login enviado', { email, password });
    alert('Login realizado com sucesso!');
  };

  return (
    <div className="login-page">
      <div className="login-card">
        <div className="login-illustration" aria-hidden="true">
          <div className="illustration-badge">Arena</div>
          <div className="illustration-ring ring-one" />
          <div className="illustration-ring ring-two" />
          <div className="illustration-ring ring-three" />
          <div className="illustration-circle" />
        </div>

        <div className="login-panel">
          <p className="login-eyebrow">Bem-vindo de volta</p>
          <h1>Entrar na sua conta</h1>

          <form className="login-form" onSubmit={handleSubmit}>
            <label className="input-group">
              <span>E-mail</span>
              <input
                type="email"
                placeholder="seu@email.com"
                value={email}
                onChange={(event) => setEmail(event.target.value)}
                required
              />
            </label>

            <label className="input-group">
              <span>Senha</span>
              <input
                type="password"
                placeholder="Digite sua senha"
                value={password}
                onChange={(event) => setPassword(event.target.value)}
                required
              />
            </label>

            <div className="login-actions">
              <label className="remember-me">
                <input type="checkbox" />
                Lembrar de mim
              </label>
              <a href="#">Esqueci a senha</a>
            </div>

            <button type="submit" className="login-button">
              Entrar
            </button>
          </form>

          <p className="signup-text">
            Ainda não tem conta? <a href="#">Crie uma agora</a>
          </p>
        </div>
      </div>
    </div>
  );
}

export default App;

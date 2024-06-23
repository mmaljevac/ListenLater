import React, { useContext, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { AppContext } from "../AppContext";

const Login = () => {
  const { setCurUser } = useContext(AppContext);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleEmailChange = (e) => {
    setEmail(e.target.value);
  };

  const handlePasswordChange = (e) => {
    setPassword(e.target.value);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    await fetch("http://localhost:8080/users/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ email, password }),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        if (response.status === 204 || response.headers.get('Content-Length') === '0') {
          return;
        }

        return response.json();
      })
      .then((data) => {
        if(data !== undefined) {
          setCurUser(data);
          sessionStorage.setItem('curUser', JSON.stringify(data));
          navigate('/');
        }
        else {
          alert('Wrong email/password!');
        }
      })
      .catch((error) => {
        console.error("Fetch error:", error);
      });
  };

  return (
    <div>
      <h2>Login</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="email">Email:</label>
          <input
            type="email"
            id="email"
            value={email}
            onChange={handleEmailChange}
            required
          />
        </div>
        <div>
          <label htmlFor="password">Password:</label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={handlePasswordChange}
            required
          />
        </div>
        <div>
          <button type="submit">Login</button>
        </div>
      </form>
      <Link to={'/register'}><p>Don't have an account? Register here</p></Link>
    </div>
  );
};

export default Login;

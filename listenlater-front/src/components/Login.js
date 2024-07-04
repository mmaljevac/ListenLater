import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Link, useNavigate } from 'react-router-dom';
import { login } from '../actions';

const Login = () => {
  const curUser = useSelector((state) => state.curUser);
  const dispatch = useDispatch();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleEmailChange = (e) => {
    setEmail(e.target.value);
  };

  const handlePasswordChange = (e) => {
    setPassword(e.target.value);
  };

  const handleLoginSuccess = (user) => {
    dispatch(login(user));
    navigate('/');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    await fetch('http://localhost:8080/users/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ email, password }),
    })
      .then((response) => {
        if (response.status === 200) {
          return response.json();
        } else if (response.status === 400) {
          alert('Wrong email/password!');
        } else {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
      })
      .then((data) => {
        if (data === undefined || data === null) {
          throw new Error('Request returned no data.');
        }
        handleLoginSuccess(data);
      })
      .catch((error) => {
        throw new Error(`Fetch error: ${error}`);
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
      <Link to={'/register'}>
        <p>Don't have an account? Register here</p>
      </Link>
    </div>
  );
};

export default Login;

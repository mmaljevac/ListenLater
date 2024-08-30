import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { logout } from "../actions";
import { BACKEND_URL } from "../constants";

const Account = () => {
  const curUser = useSelector((state) => state.curUser);
  const dispatch = useDispatch();

  const navigate = useNavigate();

  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [message, setMessage] = useState("");

  const handleLogout = () => {
    dispatch(logout());
    navigate("/login");
  };

  const handleChange = (e) => {
    const { name, value } = e.target;

    switch (name) {
      case "currentPassword":
        setCurrentPassword(value);
        break;
      case "newPassword":
        setNewPassword(value);
        break;
      case "confirmPassword":
        setConfirmPassword(value);
        break;
      default:
        break;
    }
  };

  const changePassword = async (e) => {
    e.preventDefault();

    if (newPassword === confirmPassword) {
      try {
        const response = await fetch(`${BACKEND_URL}/users/changePassword`, {
          method: "PATCH",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            username: curUser.username,
            currentPassword: currentPassword,
            newPassword: newPassword,
          }),
        });
        const payload = await response.json();
        if (response.ok) {
          alert(payload.message);
          dispatch(logout());
          navigate("/login");
        } else if (response.status === 404) {
          setMessage(payload.message);
        }
      } catch (error) {
        throw new Error(`Fetch error: ${error}`);
      }
    } else {
      setMessage("New password and confirm password do not match.");
    }
  };

  const deactivateUser = async () => {
    await fetch(
      `${BACKEND_URL}/users/updateUserStatus/DEACTIVATED/${curUser.id}`,
      {
        method: "PATCH",
      }
    )
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
      })
      .then((data) => {
        alert("Account deactivated!");
        handleLogout();
      })
      .catch((error) => {
        console.error("Fetch error:", error);
      });
  };

  return (
    <div className="fly-up">
      <h2>Log out</h2>
      <button onClick={handleLogout}>Log out</button>
      <h2>Change password</h2>
      <form onSubmit={changePassword}>
        <label>
          Current Password:
          <input
            type="password"
            name="currentPassword"
            value={currentPassword}
            onChange={handleChange}
            required
          />
        </label>
        <br />
        <label>
          New Password:
          <input
            type="password"
            name="newPassword"
            value={newPassword}
            onChange={handleChange}
            required
          />
        </label>
        <br />
        <label>
          Confirm Password:
          <input
            type="password"
            name="confirmPassword"
            value={confirmPassword}
            onChange={handleChange}
            required
          />
        </label>
        <br />
        <button type="submit">Change Password</button>
      </form>
      {message && <p style={{ color: "#1ee21e" }}>{message}</p>}
      <h2>Delete account</h2>
      <button style={{ backgroundColor: "red" }} onClick={deactivateUser}>
        Deactivate
      </button>
    </div>
  );
};

export default Account;

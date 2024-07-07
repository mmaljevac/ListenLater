import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { logout } from "../actions";

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
      await fetch(`http://localhost:8080/users/changePassword/${curUser.id}`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        body: new URLSearchParams({ // TODO
          currentPassword: currentPassword,
          newPassword: newPassword,
        }),
      })
        .then((response) => {
          console.log(response);
          if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
          }
          return response.json();
        })
        .then((data) => {
          console.log(data);
          if (data) {
            alert("Password changed!");
          }
        })
        .catch((error) => {
          console.error("Fetch error:", error);
          alert("Wrong current password entered!");
        });

      setMessage("");
    } else {
      setMessage("New password and confirm password do not match.");
    }
  };

  const deleteUser = async () => {
    await fetch(`http://localhost:8080/users/${curUser.email}`, {
      method: "DELETE",
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
      })
      .then((data) => {
        alert("Account deleted!");
        handleLogout();
      })
      .catch((error) => {
        console.error("Fetch error:", error);
      });
  };

  return (
    <div>
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
      <button style={{ backgroundColor: "red" }} onClick={deleteUser}>
        Deactivate
      </button>
    </div>
  );
};

export default Account;

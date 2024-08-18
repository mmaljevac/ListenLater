import { useEffect, useState } from "react";
import { Link, Navigate } from "react-router-dom";
import { useSelector } from "react-redux";

const Admin = () => {
  const curUser = useSelector((state) => state.curUser);

  const [users, setUsers] = useState([]);

  useEffect(() => {
    if (!(curUser && curUser.role === "ADMIN")) {
      return <Navigate to={{ pathname: "/" }} />;
    }
    fetchUsers();
  }, [curUser]);

  const fetchUsers = async () => {
    await fetch(`http://localhost:8080/users`, {
      method: "GET",
    })
      .then((response) => {
        console.log(response);
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
      })
      .then((data) => {
        if (data) {
          setUsers(data.data);
        }
      })
      .catch((error) => {
        console.error("Fetch error:", error);
      });
  };

  const handleDeleteUser = async (id) => {
    await fetch(`http://localhost:8080/users/${id}`, {
      method: "DELETE",
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
      })
      .then((data) => {
        if (data) {
          fetchUsers();
        }
      })
      .catch((error) => {
        console.error("Fetch error:", error);
      });
  };

  const updatePermission = async (user) => {
    let newRole = "USER";
    if (user.role === "USER") newRole = "ADMIN";
    await fetch(`http://localhost:8080/users/updateUserRole/${user.id}`, {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ string: newRole }),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
      })
      .then(() => {
        fetchUsers();
      })
      .catch((error) => {
        console.error("Fetch error:", error);
      });
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  return (
    <>
      {users && users.length > 0 && (
        <div className="fly-up">
          <h2>Users</h2>
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Username</th>
                <th>Role</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {users.map((user) => (
                <tr key={user.id}>
                  <td>{user.id}</td>
                  <td>{user.username}</td>
                  <td>
                    <Link onClick={() => updatePermission(user)}>
                      {user.role}
                    </Link>
                  </td>
                  <td>
                    <Link onClick={() => handleDeleteUser(user.id)}>
                      Delete
                    </Link>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </>
  );
};

export default Admin;

import { useContext, useEffect, useState } from "react";
import { AppContext } from "../AppContext";
import { Link, Navigate } from "react-router-dom";

const Admin = () => {
  const { curUser } = useContext(AppContext);

  const [users, setUsers] = useState([]);
  const [albums, setAlbums] = useState([]);

  useEffect(() => {
    if (!(curUser && curUser.admin)) {
      return <Navigate to={{ pathname: "/" }} />;
    }

    fetchUsers();
    fetchAlbums();
  }, []);

  const fetchUsers = async () => {
    await fetch(`http://localhost:8080/users`, {
      method: "GET",
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
      })
      .then((data) => {
        if (data) {
          setUsers(data);
        }
      })
      .catch((error) => {
        console.error("Fetch error:", error);
      });
  };

  const fetchAlbums = async () => {
    await fetch(`http://localhost:8080/albums`, {
      method: "GET",
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
      })
      .then((data) => {
        if (data) {
          setAlbums(data);
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
          const updatedUsers = users.filter((user) => user.id !== id);
          setUsers(updatedUsers);
          const updatedAlbums = albums.filter((album) => album.idUser !== id);
          setAlbums(updatedAlbums);
        }
      })
      .catch((error) => {
        console.error("Fetch error:", error);
      });
  };

  const handleDeleteAlbum = async (id) => {
    await fetch(`http://localhost:8080/albums/${id}`, {
      method: "DELETE",
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
      })
      .then((data) => {
        const updatedAlbums = albums.filter((album) => album.id !== id);
        setAlbums(updatedAlbums);
      })
      .catch((error) => {
        console.error("Fetch error:", error);
      });
  };

  const handleAdminPermissionChange = async (user) => {
    await fetch(`http://localhost:8080/users/updatePermissions/${user.id}`, {
      method: "PATCH",
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
      })
      .then((data) => {
        if (data) {
          alert("Permission changed!");
          setUsers(prevUsers => {
            return prevUsers.map(prevUser => {
              if (prevUser.id === user.id) {
                return { ...prevUser, admin: !user.admin };
              }
              return prevUser;
            });
          });
        }
      })
      .catch((error) => {
        console.error("Fetch error:", error);
      });
  };

  return (
    <div>
      <h2>Users</h2>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Email</th>
            <th>Admin</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {users &&
            users.map((user) => (
              <tr key={user.id}>
                <td>{user.id}</td>
                <td>{user.username}</td>
                <td>{user.email}</td>
                <td>
                  <Link onClick={() => handleAdminPermissionChange(user)}>
                    {user.admin ? "Yes" : "No"}
                  </Link>
                </td>
                <td>
                  <Link onClick={() => handleDeleteUser(user.id)}>Delete</Link>
                </td>
              </tr>
            ))}
        </tbody>
      </table>
      <h2>Saved albums</h2>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Artist</th>
            <th>UserID</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {albums &&
            albums.map((album) => (
              <tr key={album.id}>
                <td>{album.id}</td>
                <td>{album.name}</td>
                <td>{album.artist}</td>
                <td>{album.idUser}</td>
                <td>
                  <Link onClick={() => handleDeleteAlbum(album.id)}>Delete</Link>
                </td>
              </tr>
            ))}
        </tbody>
      </table>
    </div>
  );
};

export default Admin;

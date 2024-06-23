import { useContext, useEffect, useState } from "react";
import { Link, Navigate } from "react-router-dom";
import { AppContext } from "../AppContext";

const Home = () => {
  const { curUser } = useContext(AppContext);

  const [albums, setAlbums] = useState([]);

  const fetchAlbums = async () => {
    if (curUser) {
      await fetch(
        `http://localhost:8080/albums/getAlbumsByUser/${curUser.id}`,
        {
          method: "GET",
        }
      )
        .then((response) => {
          if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
          }
          if (
            response.status === 204 ||
            response.headers.get("Content-Length") === "0"
          ) {
            return;
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
    } else {
      return <Navigate to={{ pathname: "/login" }} />;
    }
  };

  useEffect(() => {
    fetchAlbums();
  }, []);

  const handleDelete = async (id) => {
    await fetch(`http://localhost:8080/albums/${id}`, {
      method: "DELETE",
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        fetchAlbums();
        return response.json();
      })
      .catch((error) => {
        console.error("Fetch error:", error);
      });
  };

  return curUser ? (
    <div className="content">
      {curUser.admin ? (
        <Link to={"/admin"} style={{ color: "red" }}>
          Admin room
        </Link>
      ) : (
        <p>Hello, {curUser.username} 👋</p>
      )}
      <ul>
        {albums.length !== 0 ? (
          albums.map((album) => (
            <li key={album.name}>
              <div
                className="closeButton"
                onClick={(e) => handleDelete(album.id)}
              >
                ❌
              </div>
              <Link
                to={`https://www.last.fm/music/${album.artist.replace(
                  / /g,
                  "+"
                )}/${album.name.replace(/ /g, "+")}`}
              >
                <img src={album.imgUrl} /> <br />
                {album.name}
                <div className="artist">{album.artist}</div>
              </Link>
            </li>
          ))
        ) : (
          <>
            <p>Click on the plus to add an album!</p>
          </>
        )}
      </ul>
    </div>
  ) : (
    <Navigate to={{ pathname: "/login" }} />
  );
};

export default Home;

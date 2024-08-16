import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { Link, Navigate } from "react-router-dom";

const SavedAlbums = () => {
  const curUser = useSelector(state => state.curUser)

  const [savedAlbums, setSavedAlbums] = useState([]);

  const fetchAlbums = async () => {
    if (curUser) {
      try {
        const response = await fetch(
          `http://localhost:8080/saved-albums/user/${curUser.id}`,
          {
            method: "GET",
          }
        );
        const payload = await response.json();
        if (response.ok) {
          setSavedAlbums(payload.data);
        } else if (response.status === 404) {
        }
      } catch (error) {
        throw new Error(`Fetch error: ${error}`);
      }
    } else {
      return <Navigate to={{ pathname: "/login" }} />;
    }
  };

  useEffect(() => {
    fetchAlbums();
  }, []);

  const handleDelete = async (id) => {
    try {
      const response = await fetch(`http://localhost:8080/saved-albums/user/${curUser.id}/album/${id}`, {
        method: "DELETE",
      });
      const payload = await response.json();
      if (response.ok) {
        fetchAlbums();
        return payload;
      } else if (response.status === 404) {
        console.log(response.message)
      }
    } catch (error) {
      throw new Error(`Fetch error: ${error}`);
    }
  };
  return curUser ? (
    <div className="content">
      <ul>
        {savedAlbums.length !== 0 ? (
          savedAlbums.map((sa) => (
            <li key={sa.album.name}>
              <div
                className="closeButton"
                onClick={(e) => handleDelete(sa.album.id)}
              >
                ❌
              </div>
              <Link
                to={`/albums/${sa.album.artist.replace(
                  / /g,
                  "+"
                )}/${sa.album.name.replace(/ /g, "+")}`}
              >
                <img src={sa.album.imgUrl} /> <br />
                {sa.album.name}
                <div className="artist">{sa.album.artist}</div>
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
}

export default SavedAlbums
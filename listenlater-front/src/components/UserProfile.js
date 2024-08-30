import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { Link, Navigate, useNavigate, useParams } from "react-router-dom";
import { BACKEND_URL } from "../constants";

const UserProfile = () => {
  const curUser = useSelector((state) => state.curUser);
  const navigate = useNavigate();

  const [savedAlbums, setSavedAlbums] = useState([]);
  const [friendStatus, setFriendStatus] = useState("");
  const [friendButtonText, setFriendButtonText] = useState("Add friend");
  const { userName, actionParam } = useParams();

  const fetchFriendStatus = async () => {
    try {
      const response = await fetch(
        `${BACKEND_URL}/friends/friend-status?curUserName=${curUser.username}&friendUserName=${userName}`,
        {
          method: "GET",
        }
      );
      const payload = await response.json();
      if (response.ok) {
        setFriendStatus(payload.data);
        if (payload.data === "pending") setFriendButtonText("Pending");
        else if (payload.data === "friend") setFriendButtonText("Unfriend");
        else if (payload.data === "") setFriendButtonText("Add friend");
      } else if (response.status === 404) {
        console.log(response);
      }
    } catch (error) {
      throw new Error(`Fetch error: ${error}`);
    }
  };

  const handleFriendButton = async () => {
    if (friendStatus === "") {
      try {
        const response = await fetch(
          `${BACKEND_URL}/invites/friend-request?curUserName=${curUser.username}&friendUserName=${userName}`,
          {
            method: "POST",
          }
        );
        const payload = await response.json();
        if (response.status === 404) {
          console.log(response);
        }
      } catch (error) {
        throw new Error(`Fetch error: ${error}`);
      }
    } else if (friendStatus === "friend") {
      try {
        const response = await fetch(
          `${BACKEND_URL}/friends/remove?curUserName=${curUser.username}&friendUserName=${userName}`,
          {
            method: "DELETE",
          }
        );
        const payload = await response.json();
        if (response.status === 404) {
          console.log(response);
        }
      } catch (error) {
        throw new Error(`Fetch error: ${error}`);
      }
    }

    fetchFriendStatus();
  };

  const fetchAlbums = async () => {
    try {
      const response = await fetch(
        `${BACKEND_URL}/saved-albums/username/${userName}`,
        {
          method: "GET",
        }
      );
      const payload = await response.json();
      if (response.ok) {
        setSavedAlbums(payload.data);
      } else if (response.status === 404) {
        alert(`Success: ${response.success}, ${response.message}`);
      }
    } catch (error) {
      throw new Error(`Fetch error: ${error}`);
    }
  };

  useEffect(() => {
    fetchFriendStatus();
    fetchAlbums();
  }, []);

  const handleDelete = async (id) => {
    try {
      const response = await fetch(
        `${BACKEND_URL}/saved-albums/user/${curUser.id}/album/${id}`,
        {
          method: "DELETE",
        }
      );
      const payload = await response.json();
      if (response.ok) {
        fetchAlbums();
        return payload;
      } else if (response.status === 404) {
        console.log(response);
      }
    } catch (error) {
      throw new Error(`Fetch error: ${error}`);
    }
  };

  return curUser ? (
    <div className="content fly-up">
      <div className="user-box fly-down">
        <div className="user-bubble" style={{ margin: "0 10px 0 0" }}>
          {userName.charAt(0)}
        </div>
        <div>{userName}</div>
      </div>
      {userName !== curUser.username && (
        <button
          onClick={() => handleFriendButton()}
          disabled={friendStatus === "pending"}
          className={friendStatus === "friend" && "gray"}
        >
          {friendButtonText}
        </button>
      )}
      <h1>
        {userName === curUser.username ? "My" : `${userName}'s`} Saved Albums
      </h1>
      <div style={{ textAlign: "center", margin: "0px 0" }}>
        <button
          onClick={() => navigate(`/user/${userName}/LISTEN_LATER`)}
          disabled={actionParam === "LISTEN_LATER"}
          style={{ margin: "0 10px" }}
        >
          🎧 Listen Later
        </button>
        <button
          onClick={() => navigate(`/user/${userName}/LIKE`)}
          disabled={actionParam === "LIKE"}
          style={{ margin: "0 10px" }}
        >
          Liked
        </button>
        <button
          onClick={() => navigate(`/user/${userName}/DISLIKE`)}
          disabled={actionParam === "DISLIKE"}
          style={{ margin: "0 10px" }}
        >
          Disliked
        </button>
      </div>
      <ul className="fly-up">
        {savedAlbums.length !== 0 ? (
          savedAlbums
            .filter((sa) => sa.action === actionParam)
            .map((sa) => (
              <li key={sa.album.name}>
                {userName === curUser.username && (
                  <div
                    className="closeButton"
                    onClick={(e) => handleDelete(sa.album.id)}
                  >
                    ❌
                  </div>
                )}

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
            {userName === curUser.username && (
              <p>Click on the plus to add an album!</p>
            )}
          </>
        )}
      </ul>
    </div>
  ) : (
    <Navigate to={{ pathname: "/login" }} />
  );
};

export default UserProfile;

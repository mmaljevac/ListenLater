import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { Link, Navigate } from "react-router-dom";

const Social = () => {
  const curUser = useSelector((state) => state.curUser);

  const [invites, setInvites] = useState([]);
  const [friends, setFriends] = useState([]);

  const fetchInvites = async () => {
    try {
      const response = await fetch(
        `http://localhost:8080/invites/${curUser.username}`,
        {
          method: "GET",
        }
      );
      const payload = await response.json();
      if (response.ok) {
        setInvites(payload.data);
        console.log("invites");
        console.log(payload);
      } else if (response.status === 404) {
        console.log(response);
      }
    } catch (error) {
      throw new Error(`Fetch error: ${error}`);
    }
  };

  const fetchFriends = async () => {
    try {
      const response = await fetch(
        `http://localhost:8080/friends/${curUser.username}`,
        {
          method: "GET",
        }
      );
      const payload = await response.json();
      if (response.ok) {
        setFriends(payload.data);
        console.log("friends");
        console.log(curUser.username);
        console.log(payload);
      } else if (response.status === 404) {
        console.log(response);
      }
    } catch (error) {
      throw new Error(`Fetch error: ${error}`);
    }
  };

  const handleAddFriend = async (invite) => {
    try {
      const response = await fetch(
        `http://localhost:8080/friends/add?curUserName=${invite.sender.username}&friendUserName=${invite.receiver.username}`,
        {
          method: "POST",
        }
      );
      const payload = await response.json();
      if (response.ok) {
        // alert("Friend added!");
      } else if (response.status === 404) {
        console.log(response);
      }
    } catch (error) {
      throw new Error(`Fetch error: ${error}`);
    }

    deleteInvite(invite.id);
  };

  const deleteInvite = async (inviteId) => {
    try {
      const response = await fetch(
        `http://localhost:8080/invites/remove/${inviteId}`,
        {
          method: "DELETE",
        }
      );
      const payload = await response.json();
      if (response.ok) {
        console.log(payload.success);
      } else if (response.status === 404) {
        console.log(response);
      }
    } catch (error) {
      throw new Error(`Fetch error: ${error}`);
    }

    fetchInvites();
    fetchFriends();
  };

  useEffect(() => {
    fetchInvites();
    fetchFriends();
  }, []);

  return curUser ? (
    <div className="content">
      {invites && invites.length > 0 && (
        <>
          <h2>Notifications</h2>
          {invites.map((invite, index) =>
            invite.inviteType === "FRIEND_REQUEST" ? (
              <div key={index} className="notification-card">
                <Link
                  to={`/user/${invite.sender.username}/LISTEN_LATER`}
                  className="user-bubble"
                  style={{ margin: "10px 0", color: "black" }}
                >
                  {invite.sender.username.charAt(0)}
                </Link>
                <div className="notification-text">
                  <Link to={`/user/${invite.sender.username}/LISTEN_LATER`}>
                    {invite.sender.username}
                  </Link>{" "}
                  wants to add you as a friend!
                </div>
                <div className="notification-buttons">
                  <button onClick={() => handleAddFriend(invite)}>
                    Accept
                  </button>
                  <button onClick={() => deleteInvite(invite.id)}>
                    Decline
                  </button>
                </div>
              </div>
            ) : invite.inviteType === "ALBUM_RECOMMENDATION" ? (
              <Link
                className="fly-up notification-card"
                to={`/albums/${invite.album.artist}/${invite.album.name}`}
                onClick={() => deleteInvite(invite.id)}
                key={index}
                style={{
                  textAlign: "center",
                  flex: "1"
                }}
              >
                <img
                  style={{
                    width: "100%",
                    borderRadius: "10px",
                  }}
                  src={invite.album.imgUrl}
                  alt="Album Cover"
                />
                <div
                  style={{
                    marginTop: "10px",
                    fontWeight: "bold",
                  }}
                >
                  {invite.album.name}
                </div>
                <div className="artist">{invite.album.artist}</div>
                <div>
                  🗨️{invite.sender.username}: {invite.message}
                </div>
              </Link>
            ) : null
          )}
        </>
      )}

      <h2>Friends</h2>
      {friends && friends.length > 0 ? (
        <div className="friends-container">
          {friends.map((friend, index) => (
            <Link
              to={`/user/${friend.username}/LISTEN_LATER`}
              key={index}
              className="friend-item"
            >
              <div className="user-bubble">{friend.username.charAt(0)}</div>
              <div style={{ margin: "10px 0", fontWeight: "bold" }}>
                {friend.username}
              </div>
            </Link>
          ))}
        </div>
      ) : (
        <Link to={`/search`}>Search for friends!</Link>
      )}
    </div>
  ) : (
    <Navigate to={{ pathname: "/login" }} />
  );
};

export default Social;

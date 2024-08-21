import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { Link, Navigate, useNavigate } from "react-router-dom";

const Social = () => {
  const curUser = useSelector((state) => state.curUser);
  const navigate = useNavigate();

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
        alert("Friend added!");
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
              <div key={index}>
                <Link to={`/user/${invite.sender.username}/LISTEN_LATER`}>
                  {invite.sender.username}{" "}
                </Link>
                wants to add you as a friend!
                <Link onClick={() => handleAddFriend(invite)}>Accept</Link>
                <Link onClick={() => deleteInvite(invite.id)}>Decline</Link>
              </div>
            ) : invite.inviteType === "ALBUM_RECOMMENDATION" ? (
              <Link
                to={`/albums/${invite.album.fullName}`}
                onClick={() => deleteInvite(invite.id)}
                key={index}
              >
                <img src={invite.album.imgUrl} alt="Album Cover" />
                <div>
                  {invite.sender.username}: {invite.message}
                </div>
              </Link>
            ) : null
          )}
        </>
      )}

      <h2>Friends</h2>
      {friends && friends.length > 0 ? (
        <>
          {friends.map((friend, index) => (
            <Link to={`/user/${friend.username}/LISTEN_LATER`} key={index}>
              <div className="user-bubble" style={{ margin: "0 10px 0 0" }}>
                {friend.username.charAt(0)}
              </div>
              <div>{friend.username}</div>
            </Link>
          ))}
        </>
      ) : (
        <div>Search for friends!</div>
      )}
    </div>
  ) : (
    <Navigate to={{ pathname: "/login" }} />
  );
};

export default Social;

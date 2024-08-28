import { Link, Navigate } from "react-router-dom";
import { useSelector } from "react-redux";
import { useEffect, useState } from "react";
import { LOCALHOST_URL } from "../constants";

const Home = () => {
  const curUser = useSelector((state) => state.curUser);

  const [inviteCount, setInviteCount] = useState(0);

  const fetchInvites = async () => {
    try {
      const response = await fetch(
        `${LOCALHOST_URL}/invites/count/${curUser.username}`,
        {
          method: "GET",
        }
      );
      const payload = await response.json();
      if (response.ok) {
        setInviteCount(payload.data);
      } else if (response.status === 404) {
        console.log(response);
      }
    } catch (error) {
      throw new Error(`Fetch error: ${error}`);
    }
  };

  useEffect(() => {
    if (curUser) fetchInvites();
  }, []);

  return curUser ? (
    <div className="content fly-up">
      <h1>Hello, {curUser.username} 👋</h1>
      <Link to={`/user/${curUser.username}/LISTEN_LATER`} className="link-box">
        <h2>🔖 My Saved Albums</h2>
      </Link>
      <Link
        to="/social"
        className={`link-box ${inviteCount > 0 ? "notification" : ""}`}
      >
        <h2>👥 Social</h2>
        {inviteCount > 0 && <h2>📥({inviteCount} new)</h2>}
      </Link>
      <Link to={"/charts"} className="link-box">
        <h2>📈 Charts</h2>
      </Link>
      <Link to={"/search"} className="link-box">
        <h2>🔎 Search</h2>
      </Link>
      {curUser.role === "ADMIN" && (
        <Link to={"/admin"} className="link-box">
          <h2>🔒 Admin Room</h2>
        </Link>
      )}
    </div>
  ) : (
    <Navigate to={{ pathname: "/login" }} />
  );
};

export default Home;

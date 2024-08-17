import { Link, Navigate } from "react-router-dom";
import { useSelector } from "react-redux";

const Home = () => {
  const curUser = useSelector(state => state.curUser)

  return curUser ? (
<div className="content">
  {curUser.admin ? (
    <Link to={"/admin"} className="link-box admin-link">
      Admin room
    </Link>
  ) : (
    <h1>Hello, {curUser.username} 👋</h1>
  )}
  <Link to={`/user/${curUser.username}/LISTEN_LATER`} className="link-box"><h2>🔖 My Saved Albums</h2></Link>
  <Link to={"/charts"} className="link-box"><h2>📈 Charts</h2></Link>
  <Link to={"/search/albums"} className="link-box"><h2>💽 Albums</h2></Link>
  <Link to={"/search/artists"} className="link-box"><h2>🎤 Artists</h2></Link>
  <Link to={"/search/users"} className="link-box"><h2>👤 Search users</h2></Link>
</div>

  ) : (
    <Navigate to={{ pathname: "/login" }} />
  );
};

export default Home;

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
    <p>Hello, {curUser.username} 👋</p>
  )}
  <Link to={"/savedAlbums/LISTEN_LATER"} className="link-box"><h2>🔖 My Saved Albums</h2></Link>
  <Link to={"/charts"} className="link-box"><h2>📈 Charts</h2></Link>
  <Link to={"/search"} className="link-box"><h2>🔎 Search</h2></Link>
</div>

  ) : (
    <Navigate to={{ pathname: "/login" }} />
  );
};

export default Home;

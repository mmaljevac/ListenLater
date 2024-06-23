import { Link, useNavigate } from "react-router-dom";
import Home from "./Home";
import { useContext, useEffect } from "react";
import { AppContext } from "../AppContext";

const Header = () => {
  const { curUser, setCurUser } = useContext(AppContext);
  const navigate = useNavigate();

  return (
    <header>
      <Link to={'/'} className="headerLeft">ListenLater</Link>
      <Link to={'/search'} className="headerMiddle">+</Link>
      {curUser ? (
        <Link to={'/account'} className="headerRight">Account</Link>
      ) : (
        <Link to={'/login'} className="headerRight">Login</Link>
      )}
      
    </header>
  );
};

export default Header;

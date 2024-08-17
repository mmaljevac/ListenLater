import { Link } from 'react-router-dom';
import { useSelector } from 'react-redux';

const Header = () => {
  const curUser = useSelector((state) => state.curUser);

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

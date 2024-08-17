import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Home from './components/Home';
import Login from './components/Login';
import Register from './components/Register';
import Search from './components/Search';
import Header from './components/Header';
import Admin from './components/Admin';
import Account from './components/Account';
import AlbumInfo from './components/AlbumInfo';
import Charts from './components/Charts';
import SavedAlbums from './components/SavedAlbums';
import ArtistInfo from './components/ArtistInfo';

function App() {
  return (
    <Router>
      <Header />
      <Routes>
        <Route path="/" element={<Home />}></Route>
        <Route path="/search/:searchParam?" element={<Search />}></Route>
        <Route path="/login" element={<Login />}></Route>
        <Route path="/account" element={<Account />}></Route>
        <Route path="/register" element={<Register />}></Route>
        <Route path="/albums/:artist/:albumName" element={<AlbumInfo />}></Route>
        <Route path="/artist/:artist" element={<ArtistInfo />}></Route>
        <Route path="/charts" element={<Charts />}></Route>
        <Route path="/savedAlbums/:actionParam" element={<SavedAlbums />}></Route>
        <Route path="/admin" element={<Admin />}></Route>
      </Routes>
    </Router>
  );
}

export default App;

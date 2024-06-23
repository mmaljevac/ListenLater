import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Home from "./components/Home";
import Login from "./components/Login";
import Register from "./components/Register";
import Search from "./components/Search";
import Header from "./components/Header";
import { AppContext } from "./AppContext";
import { useState } from "react";
import Admin from "./components/Admin";
import Account from "./components/Account";

function App() {
  const [curUser, setCurUser] = useState(JSON.parse(sessionStorage.getItem('curUser')));

  return (
    <Router>
      <AppContext.Provider
        value={{
          curUser,
          setCurUser
        }}
      >
        <Header />
        <Routes>
          <Route path="/" element={<Home />}></Route>
          <Route path="/search" element={<Search />}></Route>
          <Route path="/login" element={<Login />}></Route>
          <Route path="/account" element={<Account />}></Route>
          <Route path="/register" element={<Register />}></Route>
          <Route path="/admin" element={<Admin />}></Route>
        </Routes>
      </AppContext.Provider>
    </Router>
  );
}

export default App;

import { useEffect, useRef, useState } from "react";
import { Navigate, useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";

const Search = () => {
  const curUser = useSelector((state) => state.curUser);
  const navigate = useNavigate();

  const [searchTerm, setSearchTerm] = useState("");
  const [searchResults, setSearchResults] = useState([]);

  const searchInputRef = useRef(null);

  const handleSearch = async () => {
    if (searchTerm) {
      try {
        const response = await fetch(
          // TODO url constants
          `https://ws.audioscrobbler.com/2.0/?method=album.search&album=${searchTerm}&limit=30&api_key=6114c4f9da678af26ac5a4afc15d9c4f&format=json`
        );
        const data = await response.json();
        console.log(data)

        setSearchResults(data.results.albummatches.album);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    }
  };

  useEffect(() => {
    if (searchInputRef.current) {
      searchInputRef.current.focus();
    }
  }, []);

  const handleKeyPress = (event) => {
    if (event.key === "Enter") {
      handleSearch();
    }
  };

  return curUser ? (
    <div className="content">
      <input
        type="text"
        value={searchTerm}
        ref={searchInputRef}
        onChange={(e) => setSearchTerm(e.target.value)}
        onKeyDown={handleKeyPress}
        placeholder="Album/artist name"
        className="searchBubble"
        style={{ marginBottom: '20px' }}
      />{" "}
      <br></br>
      <button onClick={handleSearch}>Search</button>
      <ul className="seachUl">
        {searchResults.map((album) => (
          <li
            className="searchLi"
            key={album.artist + album.name}
            onClick={() => navigate(`/albums/${album.artist.replace(
                  / /g,
                  "+"
                )}/${album.name.replace(/ /g, "+")}`)}
          >
            <a>
              <div className="searchItems">
                <img src={album.image[3]["#text"]} style={{ width: '150px'}} />
                <aside>
                  <div>{album.name}</div>
                  <div className="artist">{album.artist}</div>
                </aside>
              </div>
            </a>
          </li>
        ))}
      </ul>
    </div>
  ) : (
    <Navigate to={{ pathname: "/login" }} />
  );
};

export default Search;

import { useState } from "react";
import { Navigate, useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";

const Search = () => {
  const curUser = useSelector((state) => state.curUser);
  const navigate = useNavigate();

  const [searchTerm, setSearchTerm] = useState("");
  const [searchResults, setSearchResults] = useState([]);

  const handleSearch = async () => {
    if (searchTerm) {
      try {
        const response = await fetch(
          // TODO url constants
          `https://ws.audioscrobbler.com/2.0/?method=album.search&album=${searchTerm}&limit=30&api_key=6114c4f9da678af26ac5a4afc15d9c4f&format=json`
        );
        const data = await response.json();

        setSearchResults(data.results.albummatches.album);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    }
  };

  const handleAlbumClick = async (album) => {
    const name = album.name;
    const artist = album.artist;
    const fullName = artist + "/" + name;
    const imgUrl = album.image[3]["#text"];

    try {
      const response = await fetch(
        `http://localhost:8080/saved-albums/save/user/${curUser.id}?action=LIKE`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            fullName,
            name,
            artist,
            imgUrl,
          }),
        }
      );
      const payload = await response.json();
      if (response.ok) {
        alert("Album added to your ListenLater playlist!");
      } else if (response.status === 404) {
        console.log(response.message);
      }
    } catch (error) {
      throw new Error(`Fetch error: ${error}`);
    }
  };

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
        onChange={(e) => setSearchTerm(e.target.value)}
        onKeyDown={handleKeyPress}
        placeholder="Album/artist name"
        className="searchBubble"
      />{" "}
      <br></br>
      <button onClick={handleSearch}>Search</button>
      <ul className="seachUl">
        {searchResults.map((album) => (
          <li
            className="searchLi"
            key={album.name}
            onClick={() => handleAlbumClick(album)}
          >
            <a>
              <div className="searchItems">
                <div className="addButton">+</div>
                <img src={album.image[3]["#text"]} />
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

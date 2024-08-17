import { useEffect, useRef, useState } from "react";
import { Navigate, useNavigate, useParams } from "react-router-dom";
import { useSelector } from "react-redux";

const Search = () => {
  const curUser = useSelector((state) => state.curUser);
  const navigate = useNavigate();

  const [searchTerm, setSearchTerm] = useState("");
  const [searchResults, setSearchResults] = useState([]);
  const [searchTrigger, setSearchTrigger] = useState(false);

  const { entity, searchParam } = useParams();
  const searchInputRef = useRef(null);

  const handleSearch = () => {
    if (searchTerm !== "") {
      navigate(`/search/${entity}/${searchTerm}`);
      setSearchTrigger(!searchTrigger);
    }
  };

  const fetchAlbumsArtists = async () => {
    if (searchParam && searchParam.length !== 0) {
      try {
        const response = await fetch(
          // TODO url constants
          `https://ws.audioscrobbler.com/2.0/?method=${entity.slice(
            0,
            -1
          )}.search&${entity.slice(
            0,
            -1
          )}=${searchParam}&limit=30&api_key=6114c4f9da678af26ac5a4afc15d9c4f&format=json`
        );
        const data = await response.json();

        if (entity === "albums")
          setSearchResults(data.results.albummatches.album);
        else if (entity === "artists")
          setSearchResults(data.results.artistmatches.artist);
        console.log(searchResults);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    }
  };

  const fetchUsers = async () => {
    if (searchParam && searchParam.length !== 0) {
      try {
        const response = await fetch(
          `http://localhost:8080/users?username=${searchParam}`,
          {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        const payload = await response.json();
        if (response.ok) {
          setSearchResults(payload.data);
        } else if (response.status === 404) {
          console.log(response.message);
        }
      } catch (error) {
        throw new Error(`Fetch error: ${error}`);
      }
    }
  };

  const goToInfoPage = (item) => {
    if (entity === "albums") {
      navigate(
        `/albums/${item.artist.replace(/ /g, "+")}/${item.name.replace(
          / /g,
          "+"
        )}`
      );
    } else if (entity === "artists") {
      navigate(`/artist/${item.artist.replace(/ /g, "+")}}`);
    } else if (entity === "users") {
      navigate(`/user/${item.username}/LISTEN_LATER`);
    }
  };

  const handleKeyPress = (event) => {
    if (event.key === "Enter") {
      handleSearch();
      setSearchTrigger(!searchTrigger);
    }
  };

  useEffect(() => {
    if (entity === "albums" || entity === "artists") fetchAlbumsArtists();
    else if (entity === "users") fetchUsers();

    if (searchInputRef.current) {
      searchInputRef.current.focus();
    }
  }, [searchTrigger]);

  return curUser ? (
    <div className="content">
      <input
        type="text"
        value={searchTerm}
        ref={searchInputRef}
        onChange={(e) => setSearchTerm(e.target.value)}
        onKeyDown={handleKeyPress}
        placeholder={`${entity.slice(0, -1)} name`}
        className="searchBubble"
        style={{ marginBottom: "20px" }}
      />
      <button onClick={handleSearch}>Search</button>
      <ul className="seachUl">
        {searchResults.map((item, index) => (
          <li
            className="searchLi"
            key={index}
            onClick={() => goToInfoPage(item)}
          >
            <a>
              <div className="searchItems">
                {(entity === "albums" || entity === "artists") && (
                  <img
                    src={item.image[3]["#text"]}
                    style={{
                      width: "150px",
                      borderRadius: entity === "artists" ? "50%" : undefined,
                    }}
                  />
                )}

                {entity === "users" && (
                  <div className="user-bubble">{item.username.charAt(0)}</div>
                )}

                <aside>
                  <div>{entity === "users" ? item.username : item.name}</div>
                  {entity === "albums" && (
                    <div className="artist">{item.artist}</div>
                  )}
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

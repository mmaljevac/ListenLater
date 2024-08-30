import { useEffect, useRef, useState } from "react";
import { Link, Navigate, useNavigate, useParams } from "react-router-dom";
import { useSelector } from "react-redux";
import { API_KEY, LASTFM_API_URL, BACKEND_URL } from "../constants";

const Search = () => {
  const curUser = useSelector((state) => state.curUser);
  const navigate = useNavigate();

  const [searchTerm, setSearchTerm] = useState("");
  const [albumResults, setAlbumResults] = useState([]);
  const [artistResults, setArtistResults] = useState([]);
  const [userResults, setUserResults] = useState([]);
  const [entityType, setEntityType] = useState("");
  const [searchTrigger, setSearchTrigger] = useState(false);

  const { searchParam } = useParams();
  const searchInputRef = useRef(null);

  const handleSearch = () => {
    setEntityType("");
    if (searchTerm !== "") {
      navigate(`/search/${searchTerm}`);
      setSearchTrigger(!searchTrigger);
    }
  };

  const fetchAlbumsArtists = async (entity) => {
    if (searchParam && searchParam.length !== 0) {
      try {
        const response = await fetch(
          `${LASTFM_API_URL}/2.0/?method=${entity}.search&${entity}=${searchParam}&limit=30&api_key=${API_KEY}&format=json`
        );
        const data = await response.json();

        if (entity === "album")
          setAlbumResults(data.results.albummatches.album);
        else if (entity === "artist")
          setArtistResults(data.results.artistmatches.artist);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    }
  };

  const fetchUsers = async () => {
    if (searchParam && searchParam.length !== 0) {
      try {
        const response = await fetch(
          `${BACKEND_URL}/users?username=${searchParam}`,
          {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        const payload = await response.json();
        if (response.ok) {
          setUserResults(payload.data);
        } else if (response.status === 404) {
          console.log(response.message);
        }
      } catch (error) {
        throw new Error(`Fetch error: ${error}`);
      }
    }
  };

  const handleKeyPress = (event) => {
    if (event.key === "Enter") {
      handleSearch();
      setSearchTrigger(!searchTrigger);
    }
  };

  useEffect(() => {
    fetchAlbumsArtists("album");
    fetchAlbumsArtists("artist");
    fetchUsers();

    if (searchInputRef.current) {
      searchInputRef.current.focus();
    }
    setSearchTerm(searchParam);
  }, [searchTrigger]);

  return curUser ? (
    <div className="content fly-up">
      <input
        type="text"
        value={searchTerm}
        ref={searchInputRef}
        onChange={(e) => setSearchTerm(e.target.value)}
        onKeyDown={handleKeyPress}
        placeholder={"Album / artist / user"}
        className="searchBubble"
        style={{ marginBottom: "20px" }}
      />
      <button onClick={handleSearch}>Search</button>

      {albumResults.length > 0 &&
        (entityType === "" || entityType === "album") && (
          <div className="fly-up entity-class">
            <h1 style={{ margin: "0" }}>
              <Link
                onClick={() =>
                  entityType === "" ? setEntityType("album") : setEntityType("")
                }
              >
                Albums
              </Link>
            </h1>
            <ul className="seachUl">
              {albumResults
                .slice(0, entityType !== "album" ? 3 : albumResults.length)
                .map((item, index) => (
                  <li
                    className="searchLi"
                    key={index}
                    onClick={() =>
                      navigate(
                        `/albums/${item.artist.replace(
                          / /g,
                          "+"
                        )}/${item.name.replace(/ /g, "+")}`
                      )
                    }
                  >
                    <a>
                      <div className="searchItems">
                        <img
                          src={item.image[3]["#text"]}
                          style={{
                            width: "100px",
                          }}
                        />

                        <aside>
                          <div>{item.name}</div>
                          <div className="artist">{item.artist}</div>
                        </aside>
                      </div>
                    </a>
                  </li>
                ))}
            </ul>
          </div>
        )}

      {artistResults.length > 0 &&
        (entityType === "" || entityType === "artist") && (
          <div className="fly-up">
            <h1 style={{ margin: "0" }}>
              <Link
                onClick={() =>
                  entityType === ""
                    ? setEntityType("artist")
                    : setEntityType("")
                }
              >
                Artists
              </Link>
            </h1>
            <ul className="seachUl">
              {artistResults
                .slice(0, entityType !== "artist" ? 3 : albumResults.length)
                .map((item, index) => (
                  <li
                    className="searchLi"
                    key={index}
                    onClick={() =>
                      navigate(`/artist/${item.name.replace(/ /g, "+")}`)
                    }
                  >
                    <a>
                      <div className="searchItems">
                        <img
                          src={item.image[3]["#text"]}
                          style={{
                            width: "80px",
                            borderRadius: "50%",
                          }}
                        />

                        <aside>
                          <div>{item.name}</div>
                        </aside>
                      </div>
                    </a>
                  </li>
                ))}
            </ul>
          </div>
        )}

      {userResults.length > 0 &&
        (entityType === "" || entityType === "user") && (
          <div className="fly-up">
            <h1 style={{ margin: "0" }}>
              <Link
                onClick={() =>
                  entityType === "" ? setEntityType("user") : setEntityType("")
                }
              >
                Users
              </Link>
            </h1>
            <ul className="seachUl">
              {userResults
                .slice(0, entityType !== "user" ? 3 : albumResults.length)
                .map((item, index) => (
                  <li
                    className="searchLi"
                    key={index}
                    onClick={() =>
                      navigate(`/user/${item.username}/LISTEN_LATER`)
                    }
                  >
                    <a>
                      <div className="searchItems">
                        <div
                          className="user-bubble"
                          style={{
                            width: "80px",
                            height: "80px",
                            fontSize: "40px",
                          }}
                        >
                          {item.username.charAt(0)}
                        </div>

                        <aside>
                          <div>{item.username}</div>
                        </aside>
                      </div>
                    </a>
                  </li>
                ))}
            </ul>
          </div>
        )}
    </div>
  ) : (
    <Navigate to={{ pathname: "/login" }} />
  );
};

export default Search;

import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { Link, useParams } from "react-router-dom";
import Track from "./Track";
import { API_KEY, LASTFM_API_URL, BACKEND_URL } from "../constants";

const Album = () => {
  const curUser = useSelector((state) => state.curUser);

  const { artist, albumName } = useParams();

  const [albumInfo, setAlbumInfo] = useState([]);
  const [albumSaved, setAlbumSaved] = useState("");
  const [fadeIn, setFadeIn] = useState(false);
  const [showAbout, setShowAbout] = useState(false);
  const [isRecommendClicked, setIsRecommendClicked] = useState(false);
  const [friends, setFriends] = useState([]);

  const fetchAlbumInfo = async () => {
    try {
      const response = await fetch(
        `${LASTFM_API_URL}/2.0/?method=album.getinfo&artist=${artist}&album=${albumName}&api_key=${API_KEY}&format=json`
      );
      const data = await response.json();
      setAlbumInfo(data.album);
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  const isAlbumSaved = async () => {
    try {
      const response = await fetch(
        `${BACKEND_URL}/saved-albums/album/user/${curUser.id}?fullName=${
          artist + "/" + albumName
        }`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      const payload = await response.json();
      if (response.ok) {
        setAlbumSaved(payload.data);
      } else if (response.status === 404) {
        console.log(response.message);
      }
    } catch (error) {
      throw new Error(`Fetch error: ${error}`);
    }
  };

  const handleAlbumSave = async (album, action) => {
    const name = album.name;
    const artistName = album.artist;
    const fullName = (artist + "/" + name).replace(/ /g, "+");
    const imgUrl = album.image[3]["#text"];

    try {
      const response = await fetch(
        `${BACKEND_URL}/saved-albums/save/user/${curUser.id}?action=${action}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            fullName,
            name,
            artist: artistName,
            imgUrl,
          }),
        }
      );
      const payload = await response.json();
      if (response.ok) {
        isAlbumSaved();
      } else if (response.status === 404) {
        console.log(response.message);
      }
    } catch (error) {
      throw new Error(`Fetch error: ${error}`);
    }
  };

  const fetchFriends = async () => {
    try {
      const response = await fetch(
        `${BACKEND_URL}/friends/${curUser.username}`,
        {
          method: "GET",
        }
      );
      const payload = await response.json();
      if (response.ok) {
        setFriends(payload.data);
      } else if (response.status === 404) {
        console.log(response);
      }
    } catch (error) {
      throw new Error(`Fetch error: ${error}`);
    }
  };

  const recommendAlbum = async (friendUserName) => {
    try {
      const response = await fetch(
        `${BACKEND_URL}/invites/recommend-album?curUserName=${curUser.username}&friendUserName=${friendUserName}&albumFullName=${artist}/${albumName}&message=Check this album out!`,
        {
          method: "POST",
        }
      );
      const payload = await response.json();
      if (response.ok) {
        if (payload.success) alert(`Album recommended to ${friendUserName}!`);
      } else if (response.status === 404) {
        console.log(response);
      }
    } catch (error) {
      throw new Error(`Fetch error: ${error}`);
    }
  };

  useEffect(() => {
    fetchAlbumInfo();
    isAlbumSaved();

    setFadeIn(true);

    if (showAbout) {
      window.scrollTo({
        top: document.documentElement.scrollHeight,
        behavior: "smooth",
      });
    }

    fetchFriends();
  }, [showAbout]);

  return (
    <div className="content">
      {albumInfo.name && (
        <>
          <div
            className={`${fadeIn ? "fade-in" : ""}`}
            style={{
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              marginTop: "10px",
            }}
          >
            <div style={{ position: "relative", display: "inline-block" }}>
              <img
                src={albumInfo.image[3]["#text"]}
                style={{
                  width: "350px",
                  position: "relative",
                  zIndex: 2,
                  borderRadius: "10px",
                }}
                alt="Album Cover"
              />
              <img
                src={albumInfo.image[3]["#text"]}
                style={{
                  width: "400px",
                  filter: "blur(50px)",
                  position: "absolute",
                  top: -20,
                  left: -25,
                  zIndex: 1,
                  opacity: 0.3,
                }}
                alt="Album Cover Blurred"
              />
            </div>

            <Link
              to={`https://www.youtube.com/results?search_query=${albumInfo.artist}+${albumInfo.name}`}
              target="_blank"
              style={{
                fontSize: "24px",
                fontWeight: "bold",
                marginTop: "20px",
                textAlign: "center",
              }}
              className="yt-link"
            >
              {albumInfo.name}
            </Link>
            <Link
              to={`/artist/${artist}`}
              className="artist"
              style={{
                fontSize: "18px",
                marginTop: "5px",
                textAlign: "center",
              }}
            >
              by {albumInfo.artist}
            </Link>
          </div>

          {albumInfo.tracks && (
            <div style={{ marginTop: "30px" }} className="fly-up">
              {albumInfo.tracks?.track?.map((track, index) => (
                <Track
                  key={index}
                  index={index}
                  track={track}
                  artistName={albumInfo.artist}
                />
              ))}
            </div>
          )}

          <div style={{ margin: "30px 0 20px" }} className="fly-up">
            <div style={{ textAlign: "center" }}>
              {Number(albumInfo.listeners).toLocaleString()} listeners •{" "}
              {Number(albumInfo.playcount).toLocaleString()} streams
            </div>

            {albumInfo.tags && (
              <div className="artist" style={{ textAlign: "center" }}>
                {Array.isArray(albumInfo.tags.tag) ? (
                  albumInfo.tags.tag.slice(0, 4).map((tag, index) => (
                    <span key={index}>
                      {tag.name}
                      {index < albumInfo.tags.tag.length - 2 ? " • " : ""}
                    </span>
                  ))
                ) : (
                  <span>{albumInfo.tags.tag.name}</span>
                )}
              </div>
            )}
          </div>

          <div
            style={{ textAlign: "center", margin: "20px 0" }}
            className="fly-up"
          >
            <button
              onClick={() => handleAlbumSave(albumInfo, "LISTEN_LATER")}
              disabled={albumSaved === "LISTEN_LATER"}
              style={{ margin: "0 10px" }}
            >
              {albumSaved === "LISTEN_LATER"
                ? "Added to Listen Later"
                : "➕ Listen Later"}
            </button>
            <button
              onClick={() => handleAlbumSave(albumInfo, "LIKE")}
              disabled={albumSaved === "LIKE"}
              style={{ margin: "0 10px" }}
            >
              {albumSaved === "LIKE" ? "Liked" : "👍 Like"}
            </button>
            <button
              onClick={() => handleAlbumSave(albumInfo, "DISLIKE")}
              disabled={albumSaved === "DISLIKE"}
              style={{ margin: "0 10px" }}
            >
              {albumSaved === "DISLIKE" ? "Disliked" : "👎 Dislike"}
            </button>
          </div>

          {albumSaved !== "" && friends && friends.length > 0 && (
            <>
              <button
                onClick={() => setIsRecommendClicked(!isRecommendClicked)}
                className="fly-up"
              >
                Recommend to a friend
              </button>
              <div className="friends-container" style={{ margin: "20px 0" }}>
                {isRecommendClicked &&
                  friends.map((friend, index) => (
                    <Link
                      onClick={() => recommendAlbum(friend.username)}
                      key={index}
                      className="friend-item fly-up"
                    >
                      <div
                        className="user-bubble"
                        style={{
                          width: "80px",
                          height: "80px",
                          fontSize: "40px",
                        }}
                      >
                        {friend.username.charAt(0)}
                      </div>
                      <div style={{ margin: "10px 0", fontWeight: "bold" }}>
                        {friend.username}
                      </div>
                    </Link>
                  ))}
              </div>
            </>
          )}

          {albumInfo.wiki && (
            <div className="fly-up">
              <h3 onClick={() => setShowAbout(!showAbout)}>
                <Link>
                  {!showAbout ? "Show album info" : "Hide album info"}
                </Link>
              </h3>
              {showAbout && (
                <div className="artist" style={{ textAlign: "justify" }}>
                  {albumInfo.wiki.content.split("<a")[0]}
                </div>
              )}
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default Album;

import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { Link, useParams } from "react-router-dom";

const Album = () => {
  const curUser = useSelector((state) => state.curUser);

  const { artist, albumName } = useParams();

  const [albumInfo, setAlbumInfo] = useState([]);
  const [albumSaved, setAlbumSaved] = useState("");
  const [fadeIn, setFadeIn] = useState(false);
  const [showAbout, setShowAbout] = useState(false);

  const fetchAlbumInfo = async () => {
    try {
      const response = await fetch(
        `https://ws.audioscrobbler.com/2.0/?method=album.getinfo&artist=${artist}&album=${albumName}&api_key=6114c4f9da678af26ac5a4afc15d9c4f&format=json`
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
        `http://localhost:8080/saved-albums/album/user/${curUser.id}?fullName=${
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
        `http://localhost:8080/saved-albums/save/user/${curUser.id}?action=${action}`,
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
            <div style={{ marginTop: "30px" }}>
              {albumInfo.tracks?.track?.map((track, index) => {
                const minutes = Math.floor(track.duration / 60);
                const seconds = track.duration % 60;
                const formattedDuration = `${minutes}:${
                  seconds < 10 ? "0" : ""
                }${seconds}`;

                return (
                  <div
                    key={index}
                    style={{
                      display: "flex",
                      justifyContent: "space-between",
                      margin: "10px 0",
                    }}
                  >
                    <span style={{ color: "white", fontSize: "20px" }}>
                      <Link
                        to={`https://www.youtube.com/results?search_query=${albumInfo.artist}+${track.name}`}
                        target="_blank"
                        className="yt-link"
                      >
                        {index + 1}. {track.name}
                      </Link>
                    </span>
                    <span style={{ color: "grey" }}>{formattedDuration}</span>
                  </div>
                );
              })}
            </div>
          )}

          <div style={{ margin: "30px 0 20px" }}>
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

          <div style={{ textAlign: "center", margin: "20px 0" }}>
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

          {albumInfo.wiki && (
            <>
              <h3 onClick={() => setShowAbout(!showAbout)}>
                <Link>
                  {!showAbout ? "Show album info" : "Hide album info"}
                </Link>
              </h3>
              <div style={{ marginBottom: "100px" }}>
                {showAbout && (
                  <div className="artist" style={{ textAlign: "justify" }}>
                    {albumInfo.wiki.content.split("<a")[0]}
                  </div>
                )}
              </div>
            </>
          )}
        </>
      )}
    </div>
  );
};

export default Album;

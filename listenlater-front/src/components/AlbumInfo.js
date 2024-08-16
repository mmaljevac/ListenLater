import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { Link, useParams } from "react-router-dom";

const Album = () => {
  const curUser = useSelector((state) => state.curUser);

  const { artist, albumName } = useParams();

  const [albumInfo, setAlbumInfo] = useState([]);
  const [albumSaved, setAlbumSaved] = useState("");

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
    const artist = album.artist;
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
            artist,
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
  }, []);

  return (
    <div className="content">
      {!albumInfo.name ? (
        "Loading..."
      ) : (
        <>
          <div
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

            <div
              style={{
                fontSize: "24px",
                fontWeight: "bold",
                marginTop: "20px",
                textAlign: "center",
              }}
            >
              {albumInfo.name}
            </div>
            <Link
              to="/artist/"
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
                      {index + 1}. {track.name}
                    </span>
                    <span style={{ color: "grey" }}>{formattedDuration}</span>
                  </div>
                );
              })}
            </div>
          )}

          <div style={{ textAlign: "center", margin: "20px 0" }}>
            <button
              onClick={() => handleAlbumSave(albumInfo, "LISTEN_LATER")}
              disabled={albumSaved === "LISTEN_LATER"}
              style={{ margin: "0 10px" }}
            >
              ➕ Listen Later
            </button>
            <button
              onClick={() => handleAlbumSave(albumInfo, "LIKE")}
              disabled={albumSaved === "LIKE"}
              style={{ margin: "0 10px" }}
            >
              👍 Like
            </button>
            <button
              onClick={() => handleAlbumSave(albumInfo, "DISLIKE")}
              disabled={albumSaved === "DISLIKE"}
              style={{ margin: "0 10px" }}
            >
              👎 Dislike
            </button>
          </div>

          <div style={{ textAlign: "center", marginBottom: "10px" }}>
            Playcount: {albumInfo.playcount}
          </div>

          {albumInfo.tags && (
            <div style={{ textAlign: "center", marginBottom: "10px" }}>
              Tags:{" "}
              {albumInfo.tags?.tag?.slice(0, 4).map((tag, index) => (
                <span key={index}>
                  {tag.name}
                  {index < albumInfo.tags.tag.length - 2 ? ", " : ""}
                </span>
              ))}
            </div>
          )}

          {albumInfo.wiki && (
            <div style={{ textAlign: "justify", margin: "10px 0 50px" }}>
              <hr />
              {albumInfo.wiki.summary
                .replace(/<a\b[^>]*>(.*?)<\/a>/gi, "")
                .slice(0, -1)}
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default Album;

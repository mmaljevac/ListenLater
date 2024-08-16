import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";

const Album = () => {
  const { artist, albumName } = useParams();

  const [albumInfo, setAlbumInfo] = useState([]);

  const fetchAlbumInfo = async () => {
    try {
      const response = await fetch(
        `https://ws.audioscrobbler.com/2.0/?method=album.getinfo&artist=${artist}&album=${albumName}&api_key=6114c4f9da678af26ac5a4afc15d9c4f&format=json`
      );
      const data = await response.json();
      console.log(data);
      setAlbumInfo(data.album);
      console.log(data.album);
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  useEffect(() => {
    fetchAlbumInfo();
  }, []);

  return (
    <div className="content">
      {!albumInfo.name ? (
        "Loading..."
      ) : (
        <>
          <img src={albumInfo.image[3]["#text"]} style={{ width: "350px" }} />
          <div
            style={{ fontSize: "20px", fontWeight: "bold", marginTop: "10px" }}
          >
            {albumInfo.name}
          </div>
          <Link to="/artist/" className="artist">
            by {albumInfo.artist}
          </Link>
          <hr />

          {albumInfo.tracks && (
            <>
                {albumInfo.tracks?.track?.map((track, index) => {
                  const minutes = Math.floor(track.duration / 60);
                  const seconds = track.duration % 60;
                  const formattedDuration = `${minutes}:${
                    seconds < 10 ? "0" : ""
                  }${seconds}`;

                  return (
                    <div
                      key={index}
                    >
                      <span style={{ color: "white", fontSize: '20px' }}>{index + 1} {track.name}</span> <span style={{ color: 'grey'}}>{formattedDuration}</span>
                    </div>
                  );
                })}
            </>
          )}

          <hr />
          <div>Playcount: {albumInfo.playcount}</div>
          {albumInfo.tags && (
            <div>
              Tags:{" "}
              {albumInfo.tags?.tag?.slice(0, 4).map((tag, index) => (
                <span key={index}>
                  {tag.name}
                  {index < albumInfo.tags.tag.length - 2 ? ", " : ""}
                </span>
              ))}
            </div>
          )}
          <hr />

          <div>
            {albumInfo.wiki &&
              albumInfo.wiki.summary
                .replace(/<a\b[^>]*>(.*?)<\/a>/gi, "")
                .slice(0, -1)}
          </div>
        </>
      )}
    </div>
  );
};

export default Album;

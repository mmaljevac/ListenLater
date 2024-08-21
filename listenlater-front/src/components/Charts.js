import { useEffect, useState } from "react";
import Track from "./Track";
import { Link } from "react-router-dom";

const Charts = () => {
  const [tracks, setTracks] = useState([]);
  const [artists, setArtists] = useState([]);

  const fetchCharts = async () => {
    try {
      const response = await fetch(
        `https://ws.audioscrobbler.com/2.0/?method=chart.gettoptracks&format=json&limit=10&api_key=6114c4f9da678af26ac5a4afc15d9c4f&format=json`
      );
      const data = await response.json();
      setTracks(data.tracks.track);
    } catch (error) {
      console.error("Error fetching data:", error);
    }

    try {
      const response = await fetch(
        `https://ws.audioscrobbler.com/2.0/?method=chart.gettopartists&format=json&limit=9&api_key=6114c4f9da678af26ac5a4afc15d9c4f&format=json`
      );
      const data = await response.json();
      setArtists(data.artists.artist);
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  useEffect(() => {
    fetchCharts();
  }, []);
  return (
    <div className="content fly-up">
      <h1>Charts</h1>

      <h2>🎵 Tracks</h2>
      <div>
        {tracks &&
          tracks.map((track, index) => (
            <div
              key={index}
              className="fly-up"
              style={{
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
                margin: "10px 0",
              }}
            >
              <span
                style={{
                  color: "white",
                  fontSize: "20px",
                  marginRight: "20px",
                }}
              >
                <Link
                  to={`https://www.youtube.com/results?search_query=${track.artist.name}+${track.name}`}
                  target="_blank"
                >
                  <div>
                    {index + 1}. {track.name}
                  </div>
                  <div className="artist">{track.artist.name}</div>
                </Link>
              </span>
              <span style={{ color: "grey", marginLeft: "10px" }}>
                {Number(track.playcount).toLocaleString()}
              </span>
            </div>
          ))}
      </div>

      <h2 style={{ margin: "30px 0" }}>🎤 Artists</h2>
      {artists && (
        <div
          style={{
            display: "flex",
            flexWrap: "wrap",
            gap: "20px",
            justifyContent: "space-around",
          }}
        >
          {artists.map((artist, index) => (
            <Link
              to={`/artist/${artist.name}`}
              className="fly-up"
              key={index}
              style={{
                textAlign: "center",
                flex: "1 1 calc(20% - 20px)",
                maxWidth: "calc(20% - 20px)",
              }}
            >
              <img
                src={artist.image[1]["#text"]}
                alt={`${artist.name}`}
                style={{
                  width: "70px",
                  borderRadius: "50%",
                  objectFit: "cover",
                }}
              />
              <div style={{ marginTop: "10px", fontWeight: "bold" }}>
                {artist.name}
              </div>
            </Link>
          ))}
        </div>
      )}
    </div>
  );
};

export default Charts;

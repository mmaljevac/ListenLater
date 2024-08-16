import { useEffect, useState } from "react";

const Charts = () => {
  const [tracks, setTracks] = useState([]);

  const fetchCharts = async () => {
    try {
      const response = await fetch(
        `https://ws.audioscrobbler.com/2.0/?method=chart.gettoptracks&format=json&limit=10&api_key=6114c4f9da678af26ac5a4afc15d9c4f&format=json`
      );
      const data = await response.json();
      setTracks(data.tracks.track)
      console.log(data.tracks.track);
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };
  const [activeChart, setActiveChart] = useState("tracks");

  // Funkcija za prebacivanje između "Tracks" i "Artists"
  const handleSwitch = (type) => {
    setActiveChart(type);
  };
  useEffect(() => {
    fetchCharts();
  }, []);
  return (
    <div>
    <div>
      <button onClick={() => handleSwitch("tracks")} disabled={activeChart === "tracks"}>
        Tracks
      </button>
      <button onClick={() => handleSwitch("artists")} disabled={activeChart === "artists"}>
        Artists
      </button>
    </div>

    {activeChart === "tracks" ? (
      <div>
        <h2>Top Tracks</h2>
        {/* {tracks.map((track) => (
          <li
            className="searchLi"
            key={chart.name}
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
        ))} */}
      </div>
    ) : (
      <div>
        <h2>Top Artists</h2>
        {/* Prikaz artist chartova */}
      </div>
    )}
  </div>
  )
}

export default Charts
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { useParams } from "react-router-dom";

const ArtistInfo = () => {
  
  const curUser = useSelector((state) => state.curUser);

  const { artist } = useParams();

  const [artistInfo, setArtistInfo] = useState([]);
  const [fadeIn, setFadeIn] = useState(false);

  const fetchArtistInfo = async () => {
    try {
      const response = await fetch(
        `https://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist=${artist}&api_key=6114c4f9da678af26ac5a4afc15d9c4f&format=json`
      );
      const data = await response.json();
      setArtistInfo(data.album);
      console.log(data)
      console.log(data.artist.image[3]['#text'])
    } catch (error) {
      console.error("Error fetching data:", error);
    }

  };

  useEffect(() => {
    fetchArtistInfo();

    setFadeIn(true);
  }, []);
  return (
    <div className="content">
      {artistInfo && artistInfo.name}
      {/* {artistInfo && (
        <img src={artistInfo.artist.image[3]["#text"]} />
      )} */}
    </div>
  )
}

export default ArtistInfo
import React from "react";
import { Link } from "react-router-dom";

const Track = ({ index, track, streams, artistName }) => {
  const minutes = Math.floor(track.duration / 60);
  const seconds = track.duration % 60;
  const formattedDuration = `${minutes}:${seconds < 10 ? "0" : ""}${seconds}`;

  return (
    <div
      style={{
        display: "flex",
        justifyContent: "space-between",
        margin: "10px 0",
      }}
    >
      <span style={{ color: "white", fontSize: "20px", marginRight: "20px" }}>
        <Link
          to={`https://www.youtube.com/results?search_query=${artistName}+${track.name}`}
          target="_blank"
          className="yt-link"
        >
          {index + 1}. {track.name}
        </Link>
      </span>
      <span style={{ color: "grey", marginLeft: "10px" }}>{track.duration ? formattedDuration : streams}</span>
    </div>
  );
};


export default Track;

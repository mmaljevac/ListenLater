import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { Link, useParams } from "react-router-dom";
import Track from "./Track";

const ArtistInfo = () => {
  const curUser = useSelector((state) => state.curUser);

  const { artist } = useParams();

  const [artistInfo, setArtistInfo] = useState([]);
  const [artistImg, setArtistImg] = useState([]);
  const [topTracks, setTopTracks] = useState([]);
  const [topAlbums, setTopAlbums] = useState([]);
  const [showAbout, setShowAbout] = useState(false);
  const [fadeIn, setFadeIn] = useState(false);

  const fetchArtistInfo = async (artistName) => {
    let mbid;

    // artist info
    try {
      const response = await fetch(
        `https://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist=${artistName}&api_key=6114c4f9da678af26ac5a4afc15d9c4f&format=json`
      );
      const data = await response.json();
      setArtistInfo(data.artist);
      mbid = data.artist.mbid;
    } catch (error) {
      console.error("Error fetching data:", error);
    }

    // top tracks
    try {
      const response = await fetch(
        `https://ws.audioscrobbler.com/2.0/?method=artist.gettoptracks&artist=${artistName}&api_key=6114c4f9da678af26ac5a4afc15d9c4f&format=json&limit=10`
      );
      const data = await response.json();
      setTopTracks(data.toptracks.track);
    } catch (error) {
      console.error("Error fetching data:", error);
    }

    // top albums
    try {
      const response = await fetch(
        `https://ws.audioscrobbler.com/2.0/?method=artist.gettopalbums&artist=${artistName}&api_key=6114c4f9da678af26ac5a4afc15d9c4f&format=json&limit=5`
      );
      const data = await response.json();
      setTopAlbums(data.topalbums.album);
    } catch (error) {
      console.error("Error fetching data:", error);
    }

    // img from mbid
    try {
      setArtistImg("");
      const response = await fetch(
        `https://musicbrainz.org/ws/2/artist/${mbid}?inc=url-rels&fmt=json`
      );
      const data = await response.json();

      if (!data.relations) return;

      for (let i = 0; i < data.relations.length; i++) {
        if (data.relations[i].type === "image") {
          console.log("IMAGE");
          let image_url = data.relations[i].url.resource;
          if (
            image_url.startsWith("https://commons.wikimedia.org/wiki/File:")
          ) {
            const filename = image_url.substring(
              image_url.lastIndexOf("/") + 1
            );
            image_url =
              "https://commons.wikimedia.org/wiki/Special:Redirect/file/" +
              filename;
          }
          setArtistImg(image_url);
        }
      }
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  useEffect(() => {
    fetchArtistInfo(artist);

    setFadeIn(true);
  }, [artist]);
  return (
    <>
      {artistInfo.name && (
        <div className={`${fadeIn ? "fade-in" : ""} content`}>
          <div style={{ display: "flex", alignItems: "center", margin: '30px 0' }}>
            <div
              style={{
                position: "relative",
                display: "inline-block",
                width: "200px",
                height: "200px",
              }}
            >
              <div
                style={{
                  position: "absolute",
                  top: 0,
                  left: 0,
                  width: "100%",
                  height: "100%",
                  borderRadius: "1000px",
                  overflow: "hidden",
                  filter: "blur(50px)",
                  opacity: '0.7',
                  zIndex: 1,
                }}
              >
                <img
                  src={
                    artistImg !== ""
                      ? artistImg
                      : "https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png"
                  }
                  style={{
                    width: "100%",
                    height: "100%",
                    objectFit: "cover",
                    borderRadius: "1000px",
                  }}
                  alt="Album Cover"
                />
              </div>
              <div
                style={{
                  position: "relative",
                  width: "100%",
                  height: "100%",
                  borderRadius: "1000px",
                  overflow: "hidden",
                  zIndex: 2,
                }}
              >
                <img
                  src={
                    artistImg !== ""
                      ? artistImg
                      : "https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png"
                  }
                  style={{
                    width: "100%",
                    height: "100%",
                    objectFit: "cover",
                    position: "absolute",
                    borderRadius: "1000px",
                  }}
                  alt="Album Cover"
                />
              </div>
            </div>
            <h1 style={{ marginLeft: "20px" }}>
              <Link
                to={`https://www.youtube.com/results?search_query=${artist}`}
                target="_blank"
              >
                {artistInfo.name}
              </Link>
            </h1>
          </div>

          <h2 style={{ margin: "50 0 0" }} className="fly-up">
            Top tracks
          </h2>
          {topTracks && (
            <div>
              {topTracks?.map((track, index) => (
                <div className="fly-up">
                  <Track
                    key={index}
                    index={index}
                    track={track}
                    streams={Number(track.playcount).toLocaleString()}
                    artistName={artist}
                  />
                </div>
              ))}
            </div>
          )}

          <h2 className="fly-up" style={{ margin: '40px 0' }}>Top albums</h2>
          <div style={{ display: "flex" }}>
            {Array.isArray(topAlbums) &&
              topAlbums.map((album, index) => (
                <Link
                  className="fly-up"
                  to={`/albums/${artist}/${album.name.replace(/ /g, "+")}`}
                  key={index}
                  style={{
                    textAlign: "center",
                    flex: "1",
                    maxWidth: "20%",
                    margin: "0 5px",
                  }}
                >
                  <img
                    style={{
                      width: "100%",
                      maxWidth: "180px",
                      borderRadius: "10px",
                    }}
                    src={album.image[3]["#text"]}
                    alt={album.name}
                  />
                  <div
                    style={{
                      marginTop: "10px",
                      fontWeight: "bold",
                    }}
                  >
                    {album.name}
                  </div>
                  <div style={{ fontSize: "14px", color: "gray" }}>
                    {Number(album.playcount).toLocaleString()}
                  </div>
                </Link>
              ))}
          </div>

          <div style={{ margin: "30px 0 20px" }} className="fly-up">
            <div style={{ textAlign: "center" }}>
              {Number(artistInfo.stats.listeners).toLocaleString()} listeners •{" "}
              {Number(artistInfo.stats.playcount).toLocaleString()} streams
            </div>

            {artistInfo.tags && (
              <div className="artist fly-up" style={{ textAlign: "center" }}>
                {Array.isArray(artistInfo.tags.tag) ? (
                  artistInfo.tags.tag.map((tag, index) => (
                    <span key={index}>
                      {tag.name}
                      {index < artistInfo.tags.tag.length - 1 ? " • " : ""}
                    </span>
                  ))
                ) : (
                  <span>{artistInfo.tags.tag.name}</span>
                )}
              </div>
            )}
          </div>

          <h3 className="fly-up">Similar artists</h3>
          {artistInfo.similar.artist.map((a, index) => {
            <Link to={`/artist/${a.name}`} key={index}>
              {a.name}
            </Link>;
          })}

          {artistInfo.similar.artist && (
            <div style={{ textAlign: "center", fontWeight: "600", margin: '0 0 30px' }}>
              {Array.isArray(artistInfo.similar.artist) ? (
                artistInfo.similar.artist.map((a, index) => (
                  <Link to={`/artist/${a.name.replace(/ /g, "+")}`} key={index}>
                    {" "}
                    🎤 {a.name}
                  </Link>
                ))
              ) : (
                <span>{artistInfo.tags.a.name}</span>
              )}
            </div>
          )}

          {artistInfo.bio && (
            <div className="fly-up">
              <h3 onClick={() => setShowAbout(!showAbout)}>
                <Link>
                  {!showAbout ? "Show artist info" : "Hide artist info"}
                </Link>
              </h3>
              {showAbout && (
                <div className="artist" style={{ textAlign: "justify" }}>
                  {artistInfo.bio.content.split("<a")[0]}
                </div>
              )}
            </div>
          )}
        </div>
      )}
    </>
  );
};

export default ArtistInfo;

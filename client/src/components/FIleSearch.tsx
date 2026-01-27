import { useState } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
interface Video {
    id: number,
    fileName: string,
    filePath: string
}
const FileSearch = () => {
  const [filePath, setFilePath] = useState<string>("");
  const [message, setMessage] = useState<string>("");

  const [fileData, setFileData] = useState<Video[]>([]);

  const handleClick = async () => {
    try {
      const response = await axios.post("http://localhost:8080/api/v1/path", {
        filePath: filePath,
      });
      setFileData(response.data);
    } catch (e: unknown) {
      setMessage("NOT FOUND");
    }
  };

  return (
    <>
      <input
        type="text"
        placeholder="Enter folder path"
        value={filePath}
        onChange={(e) => setFilePath(e.target.value)}
      />
      <button onClick={handleClick}>Scan</button>
      {fileData ? (
        <>{fileData.map((file)=>{ return (
        <li key={file.id} ><Link to={`/play/${file.id}`}>{file.fileName}</Link></li>)})}</>
      ) : (
        <>
          <p>Waiting for files to load ... </p>
        </>
      )}
      <p>{message}</p>
    </>
  );
};

export default FileSearch;

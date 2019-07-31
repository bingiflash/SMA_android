<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "twitts";
$conn = mysqli_connect($servername, $username, $password, $dbname);
if (!$conn)
    die("Connection failed: " . mysqli_connect_error());

switch ($_GET["work"])
{
	case "register":
			$output=array();
			if(!empty($_GET["Loginname"]) && !empty($_GET["Password"]) && !empty($_GET["email"]) && !empty($_GET["location"]))
			{
				$sql = "INSERT INTO user VALUES(null,'".$_GET["Loginname"]."','".$_GET["Password"]."','".$_GET["email"]."','".$_GET["location"]."',NOW())";
				$result = mysqli_query($conn, $sql);
				if($result)
					$output[] = "success";
				else
					$output[] = "failure";
			}
			header('Content-Type: application/json; charset=utf-8');
			echo Json_encode($output);
			break;			
	case "login" :
			if(!empty($_GET["Loginname"]) && !empty($_GET["Password"]))
			{
				$sql = "SELECT * from user where username=\"".$_GET["Loginname"]." \"and password = \"".$_GET["Password"]."\"";
				$result = mysqli_query($conn, $sql);
				if(mysqli_num_rows($result) == 1)
				{
					$user = mysqli_fetch_assoc($result);
					$output = array(
					"success",
					($user["uid"]),
					($user["username"])
					);
				}
				else
					$output = array("failure");
				
			}
			header('Content-Type: application/json; charset=utf-8');
			echo Json_encode($output);
			break;
	case "post" :
			if(!empty($_GET["post"]))
			{
				$sql = "INSERT INTO twitts VALUES(null,".$_GET["uid"].",\"".$_GET["post"]."\",NOW())";
				$result = mysqli_query($conn, $sql);
				if($result)
					$output = array("success");
				else
					$output = array("failure");
			}
			header('Content-Type: application/json; charset=utf-8');
			echo Json_encode($output);
			break;
	case "messages" :
			$output = array();
			$sql = "select distinct username,body from user,(SELECT sender_id, body from message,user where message.receiver_id = ".$_GET["uid"].") as temp1 where temp1.sender_id=user.uid";
			$result = mysqli_query($conn, $sql);
			$i = mysqli_num_rows($result);
			if($i>0)
			{
				$output[]="success";
				while($i--)
				{
					$row = mysqli_fetch_assoc($result);	
					$output[] = array($row["username"],$row["body"]);
				}
			}
			else
				$output[] = "failure";
			header('Content-Type: application/json; charset=utf-8');
			echo Json_encode($output);
			break;
	case "mostlikes" :
			$output = array();
			$sql = "SELECT uid, body from twitts t, (SELECT tid from (SELECT count(like_id) AS no_likes, tid from thumb GROUP BY tid) as temp1 where no_likes = (SELECT max(no_likes) from (SELECT count(like_id) AS no_likes, tid from thumb GROUP BY tid) as temp2)) AS temp3 where t.tid=temp3.tid";
			$result = mysqli_query($conn, $sql);
			$i = mysqli_num_rows($result);
			if($i)
				while($i--)
				{
					$row=mysqli_fetch_assoc($result);
					$sql1="SELECT username from user where uid = ".$row['uid'];
					$result1= mysqli_query($conn,$sql1);
					$output[] = array(mysqli_fetch_assoc($result1)['username'],$row["body"]);
				}
			header('Content-Type: application/json; charset=utf-8');
			echo Json_encode($output);
			break;
	case "wordpost" :
			$output = array();
			if(!empty($_GET["word"]))
			{
			$sql = "SELECT location,count(body) from user u ,(select uid,body from twitts where (body LIKE '".$_GET["word"]." %') or (body like '% ".$_GET["word"]."') or (body LIKE '%".$_GET["word"]." %' and body like '% ".$_GET["word"]."%') or (body like '".$_GET["word"]."') or (body like '".$_GET["word"].".')) as temp1 where temp1.uid = u.uid group by location";
			$result = mysqli_query($conn, $sql);
			$i=mysqli_num_rows($result);
			if($i)
			{
				$output[] = "success";
				while($i--)
				{
					$row = mysqli_fetch_assoc($result);
					$output[] = array($row["location"],$row["count(body)"]);
				}
			}
			else
				$output[]= "failure";
			}
			header('Content-Type: application/json; charset=utf-8');
			echo Json_encode($output);
			break;
	case "userpost" :
			$output = array();
			if(!empty($_GET["username"]))
			{
				$sql = "select body from twitts,user where user.uid = twitts.uid and user.username = \"".$_GET["username"]."\"";$result = mysqli_query($conn, $sql);
				$i = mysqli_num_rows($result);
				if($i)
				{
					$output[] = "success";
					while($i--)	
						$output[] = mysqli_fetch_assoc($result)["body"];
				}
				else
					$output[] = "failure";
			}
			header('Content-Type: application/json; charset=utf-8');
			echo Json_encode($output);
			break;
}
mysqli_close($conn);
?>
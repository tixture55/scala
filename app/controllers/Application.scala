package controllers

import play.api.mvc._

import scala.collection.mutable.Map
import scala.collection.mutable.Buffer
import scala.collection.mutable.HashMap
import java.sql.{DriverManager, Connection, Statement, ResultSet,SQLException}

trait Monoid[T] {
  def zero: T
  def add(x: T, y: T): T
}

trait MyMap[K, V] {
  def +=(kv: (K, V))
  def lookup(key: K): Option[V]
}


class AList[K, V](keyValues: (K, V)*) extends MyMap[K, V] {
  val items: Buffer[(K, V)] = keyValues.toBuffer

  def +=(kv: (K, V)) {
    items += kv
  }

  def lookup(key: K): Option[V] = {
    for ((k, v) <- items) {
      if (k == key)
        return Some(v)
    }
    return None
  }
}

class SimpleClass(id:String) {
  // インスタンス化のタイミングで実行される。
  println("create SimpleClass id:" + id)
}
class Application extends Controller {

	lazy val list = List(1, 2, 3)
	val hoge = new SimpleClass("test")
	val original = HashMap("A" -> 123, "B" -> 456)
	private[this] val supplier = () => new HashMap[Int, String]
	val swap: HashMap[Int, String] = swap2[Int, String, HashMap[Int, String], HashMap[String, Int]](original, supplier)
	val calclate = new AList()
        val cache = collection.mutable.Map[String, String]()
	val getMap = Map(1 -> "a", 2 -> "b", 3 -> "c") getOrElse (1, "d")
var capacity = 10
	var count = 0
	var factor = 5


	def page(id : String) {
        	println(1)
	}

	def index() = Action { implicit request =>
		def apply(f: Int => String, v: Int) = f(v)
    	lazy val decorator = new Decorator("[", "]")

		Class.forName("com.mysql.jdbc.Driver").newInstance();
		var con =
            DriverManager.getConnection("jdbc:mysql://localhost/test?" +
                                   "user=root&password=xg23y91a");
        try {
           var stmt = con.createStatement()
           var rs = stmt.executeQuery("SELECT * FROM Auth")
           while (rs.next()){
              print(rs.getString(1) + " ")
              print(rs.getString(2) + " ")
              print(rs.getString(3) + " ")
              print(rs.getString(4) + " ")
              print(rs.getString(5) + " ")
              print(rs.getString(6) + " ")
              println(rs.getString(7))
          }
          stmt.close()
      } catch {
         case e:SQLException => println("Database error "+e)

         case e:Throwable => {
           println("Some other exception type:")
           e.printStackTrace()
         }
      } finally {
         con.close()
      }
		val hash = new SimpleHashtable
		hash.put("a","af")
		hash.toString
		Ok(views.html.shu(apply(decorator.layout, 7) + "Hello World!" + hash))
		//Ok(apply(decorator.layout, 7) + "Hello World!" + hash)

	}
	def swap2[V, K, R <: Map[V, K], P <: Map[K, V]](origin: P, supplier: () => R): R = {
		val result = supplier()
			origin foreach (result += _.swap)
			result
	}
}

class Decorator(left: String, right: String) {
   def layout[A](x: A) = left + x.toString() + right
 }

class SimpleHashtable  {
	var capacity = 10
	var count = 0
	var factor = 5
	var table = new Array[Entry](capacity)

	def put(key: String, value: String) {
	  println( "putting "+key)
	  if( !putInternal( key, value )) {
	    resizeTable()
	    putInternal( key, value )
	  }
	  count +=1
	}

	def putInternal( key: String, value: String ):Boolean = {
	  println("\n")
	  println( "key "+key )
	  println( "Hashcode "+key.hashCode() )
	  println( "capacity "+capacity )
	  println( "count "+count )
	  var index = abs(key.hashCode()) % capacity
	  println( "index "+index )
	  println("\n")
	  if( table(index) == null ) {
	    table(index) = new Entry(key,value)
	    true
	  } else {
	    false
	  }
	}

	def abs( number: Int ): Int =
	  if( number > 0) number else (-1 * number)

	def resizeTable() {
	  println("Resizing...")
	  var newSize: Int = capacity * factor
	  var newTable = new Array[Entry](newSize)
	  var index = 0
	  for( i <- 0 to capacity-1 ) {
	    if( table(i) != null ) {
	      index = abs( table(i).key.hashCode() ) % newSize
	      newTable(index) = table(i)
	    }
	  }
	  table = null;
	  table = newTable
	  capacity = newSize
	  println( "After resize capacity =" +capacity)
	}

	def get( key: String ): String = {
	  println( "getting key =["+key+"]")
	  println( "hashcode =["+key.hashCode()+"]")
	  println( "capacity =["+capacity+"]")
	  println( "entry = ["+table(key.hashCode % capacity)+"]")
	  table(abs(key.hashCode()) % capacity).value
	}
}

class Hashtable {

	var size = 10
	var count = 0
	val factor = 5
	var table = new Array[EntryHolder](size)

	def put(key: String, value: String) {
		println( "putting "+key)
		if( !putInternal( key, value )) {
			resizeTable()
			putInternal( key, value )
		}
		count+=1;
		println( "table: " +this.toString)
	}

	override def toString(): String = {
	  var builder = new StringBuilder("[")
	  for( i <- 0 to size-1 ) {
	    if( table(i) == null ) builder.append("null")
	    else builder.append(table(i).toString)
	  }
	  builder.append("]")
	  builder.toString
	}

	def putInternal(key:String, value:String): Boolean = {
		var index = abs( key.hashCode % size )
		var holder: EntryHolder =  table(index)

		if( holder == null ) {
		  holder = new EntryHolder
		  table(index) = holder
		}
		if( holder.hasRoom() ) {
			holder.add( new Entry( key, value ))
			true
		} else {
			false
		}
	}

	def abs( number: Int ): Int =
	  if( number > 0) number else (-1 * number)

	def resizeTable() {
		println( "resizing...");
		size = size * factor

		val oldTable = table
		var newTable = new Array[EntryHolder](size)
		var holder: EntryHolder = null
		var entry : Entry = null

		table = newTable

		for( i <- 0 to oldTable.length-1) {
			holder = oldTable(i)
			if( holder != null ) {
				for( j <- 0 to holder.count-1) {
					entry = holder.entries(j)
					putInternal( entry.key, entry.value )
				}
			}
		}
		table = null
		table = newTable
	}

	def get( key: String ): String = {
	  println( "getting key =["+key+"]")
	  println( "hashcode =["+key.hashCode()+"]")
	  println( "capacity =["+size+"]")
	  println( "entryHolder = ["+table(key.hashCode % size)+"]")
	  val entry = table(abs(key.hashCode()) % size).getEntry( key )
	  println( "entry =["+entry+"]")
	  if( entry != null )
	    entry.key
	  else
	    null
	}

}

class EntryHolder {
	val size: Int = 10
	var entries = new Array[Entry](size)
	var count: Int = 0

	def getEntry( key: String ) : Entry = {
	  for( i <- 0 to count-1 ) {
	    if( key.equals( entries(i).key )) {
	      return entries(i)
		}
	  }
	  null
	}
	def hasRoom(): Boolean =  {
	  println( "count = "+count+" size = "+size)
		if( count < size ) {
			true
		} else {
			false
		}
	}

	def add( entry: Entry ) {
		var replace = false
		if( count != 0 ) {
			for( index <- 0 to count - 1) {
				if( entries(index) != null && entries(index).key == entry.key ) {
					entries(index) = entry
					replace = true
				}
			}
		}
		if( !replace ) {
			count = count+1
			entries(count-1) = entry
		}
	}
	override def toString(): String = {
	  var builder = new StringBuilder("[")
	  for( i <- 0 to size-1 ) {
	    if( i > 1 ) builder.append(", ")
	    if( entries(i) == null )
	      builder.append("null")
	    else builder.append( entries(i) )
	  }
		  builder.append("]")
		  builder.toString()
	}
}

class Entry( var key: String, var value: String) {
  override def toString(): String = {
    var builder = new StringBuilder("[key=")
    	.append( key )
    	.append(", value=")
    	.append( value )
    	.append("]")
    	builder.toString
  }
}

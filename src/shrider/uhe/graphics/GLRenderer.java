package shrider.uhe.graphics;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.Matrix;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

public class GLRenderer implements Renderer {
	
	GL10 gl;

	private float[] light0Amb = {.4f, .4f, .4f, .3f};
	private float[] light0Dif = {1f, 1f, 1f, .7f};
	private float[] light0Pos = {0f, 0f, 0f, 1f};
	private float[] matAmb = {.3f, .3f, .3f, .5f};
	private float[] matDif = {0f,.3f,.1f,.5f};
	private float[] matSpec = {.7f, 1f, .7f, 1f};
	private float[] matAmbT = {.3f, .3f, .3f, .5f};
	private float[] matDifT = {.3f,.1f,0f,.5f};
	private float[] matSpecT = {1f, .7f, .7f, 1f};
	
	private float[] transRot;
	private float[] transTran;
	private float[] transScale;
	
	private float[] cfWorld;
	private float[] cfSquare;
	private float[] cfPyramid;
	private float[] cfLight;
	private float[] currMatrix = cfWorld;
	
	private FloatBuffer bufVer, bufNorm, bufCol;
	private ShortBuffer bufDraw;
	private FloatBuffer bufVerT, bufNormT, bufColT;
	private ShortBuffer bufDrawT;
	private float[] arrVer = {	-.5f,.5f,0f, 	//0
								-.5f,-.5f,0f,	//1
								.5f,-.5f,0f,	//2
								.5f,.5f,0f,		//3
								-.5f,.5f,-1f,	//4
								-.5f,-.5f,-1f,	//5
								.5f,-.5f,-1f,	//6
								.5f,.57f,-1f		//7
							 };
	private short[] arrDraw = {	0,1,2,
								0,2,3,//front
								7,6,5,
								7,5,4,//back
								0,5,1,
								4,5,0,//left
								3,2,6,
								3,6,7,//right?
								0,3,4,
								3,7,4,//top
								1,5,6,
								2,1,6
							  };
	private float[] arrNorm = {	-1f,1f,1f,
								-1f,-1f,1f,
								1f,-1f,1f,
								1f,1f,1f,
								-1f,1f,-1f,
								-1f,-1f,-1f,
								1f,-1f,-1f,
								1f,1f,-1f
								};
	private float[] arrCol = {	1f,0,0,1f,
								0,1f,0,1f,
								0,0,1f,1f,
								1f,1f,0,1f,
								1f,0,1f,1f,
								0f,1f,1f,1f,
								0f,0f,0f,1f,
								.4f,1f,1f,1f
							 };
	
	private float[] arrVerT = {	0f,0f,-.25f, 	//0
			-.5f,-.5f,0f,	//1
			.5f,-.5f,0f,	//2
			-.5f,-.5f,-1f,	//3
			.5f,-.5f,-1f,	//4
		 };
	private short[] arrDrawT = {	0,1,2,//front
			0,4,3,//back
			0,2,4,
			0,3,1,
			2,1,3,
			2,3,4
		  };
	private float[] arrNormT = {	0,1f,0
			-1f,-1f,1f,
			1f,-1f,1f,
			-1f,-1f,-1f,
			1f,-1f,-1f
			};
	private float[] arrColT = {	1f,0,0,1f,
			0,1f,0,1f,
			0,0,1f,1f,
			1f,1f,0,1f,
			1f,0,1f,1f,
		 };
	boolean isAnimating = true;
	Context mCtx;

	public GLRenderer(Context parent){
		super();
		mCtx = parent;
		isAnimating = true;
		transRot = new float[16];
		transTran = new float[16];
		transScale = new float[16];
		cfWorld = new float[16];
		cfSquare = new float[16];
		cfPyramid = new float[16];
		cfLight = new float[16];
		
		for (int i=0;i<16;i+=5){ //everything in here becomes I
			cfWorld[i] = 1f;
			cfSquare[i] = 1f;
			cfPyramid[i] = 1f;
			cfLight[i] = 1f;
			transRot[i] = 1f;
			transTran[i] = 1f;
			transScale[i] = 1f;
		}
			
	}
	@Override
	public synchronized void onDrawFrame(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glClearColor(.1f, .1f, .1f, 1f);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		gl.glLoadIdentity();
		gl.glTranslatef(0, 0, -2f);
		gl.glMultMatrixf(cfWorld, 0);
		
		if (isAnimating){
			gl.glPushMatrix();
			
			gl.glPopMatrix();
		}
		
		//begin the actual render
		gl.glPushMatrix();
		gl.glMultMatrixf(cfLight, 0);
		gl.glPushMatrix();
		gl.glScalef(.2f, .2f, .2f);
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_DIFFUSE, matDif, 0);
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SPECULAR, matSpec, 0);
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_AMBIENT, matAmb, 0);
		
		gl.glVertexPointer(3,GL10.GL_FLOAT,0,bufVer);
		gl.glColorPointer(4,GL10.GL_FLOAT,0,bufCol);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, bufNorm);
		//draw square
		gl.glTranslatef(0f, 0f, -2f);
		gl.glMultMatrixf(cfSquare,0);
		gl.glDrawElements(GL10.GL_TRIANGLES, arrDraw.length, GL10.GL_UNSIGNED_SHORT, bufDraw);
		gl.glPopMatrix();
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, light0Pos, 0);
		gl.glPopMatrix();
		
		gl.glEnable(GL10.GL_COLOR_MATERIAL);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		
		
		gl.glPushMatrix();
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_DIFFUSE, matDif, 0);
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SPECULAR, matSpec, 0);
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_AMBIENT, matAmb, 0);
		
		gl.glVertexPointer(3,GL10.GL_FLOAT,0,bufVer);
		gl.glColorPointer(4,GL10.GL_FLOAT,0,bufCol);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, bufNorm);
		//draw square
		gl.glTranslatef(0f, 0f, -2f);
		gl.glMultMatrixf(cfSquare,0);
		gl.glDrawElements(GL10.GL_TRIANGLES, arrDraw.length, GL10.GL_UNSIGNED_SHORT, bufDraw);
		gl.glPopMatrix();
		
		
		gl.glPushMatrix();
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_DIFFUSE, matDifT, 0);
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SPECULAR, matSpecT, 0);
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_AMBIENT, matAmbT, 0);
		gl.glVertexPointer(3,GL10.GL_FLOAT,0,bufVerT);
		gl.glColorPointer(4,GL10.GL_FLOAT,0,bufColT);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, bufNormT);
		//draw triangle
		gl.glMultMatrixf(cfPyramid,0);
		gl.glDrawElements(GL10.GL_TRIANGLES, arrDrawT.length, GL10.GL_UNSIGNED_SHORT, bufDrawT);
		gl.glPopMatrix();
		
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		
		boolean isLandscape = width > height;
		float aspect = (float) width / height;
		GLU.gluPerspective(gl, 60, aspect, .1f, 20.0f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		this.gl = gl;
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glShadeModel(GL10.GL_SMOOTH);
		
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_LIGHT0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, light0Amb, 0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, light0Dif, 0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, light0Pos, 0);
		gl.glLightf(GL10.GL_LIGHT0, GL10.GL_LINEAR_ATTENUATION, .5f);
		gl.glLightf(GL10.GL_LIGHT0, GL10.GL_LINEAR_ATTENUATION, .1f);
		
		gl.glLightModelfv(GL10.GL_LIGHT_MODEL_AMBIENT, light0Amb, 0);
		
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		
		initArrays();
		
		currMatrix = cfWorld;
		
	}

	private void initArrays(){
		
		ByteBuffer vertexBuf = ByteBuffer.allocateDirect(4*arrVer.length);
		vertexBuf.order(ByteOrder.nativeOrder());
		bufVer = vertexBuf.asFloatBuffer();
		bufVer.put(arrVer);
		vertexBuf = ByteBuffer.allocateDirect(4*arrCol.length);
		vertexBuf.order(ByteOrder.nativeOrder());
		bufCol=vertexBuf.asFloatBuffer();
		bufCol.put(arrCol);
		vertexBuf = ByteBuffer.allocateDirect(2*arrDraw.length);
		vertexBuf.order(ByteOrder.nativeOrder());
		bufDraw=vertexBuf.asShortBuffer();
		bufDraw.put(arrDraw);
		vertexBuf = ByteBuffer.allocateDirect(4*arrNorm.length);
		vertexBuf.order(ByteOrder.nativeOrder());
		bufNorm=vertexBuf.asFloatBuffer();
		bufNorm.put(arrNorm);
        
		bufVer.position(0);
		bufCol.position(0);
		bufNorm.position(0);
		bufDraw.position(0);
		
		vertexBuf = ByteBuffer.allocateDirect(4*arrVerT.length);
		vertexBuf.order(ByteOrder.nativeOrder());
		bufVerT = vertexBuf.asFloatBuffer();
		bufVerT.put(arrVerT);
		vertexBuf = ByteBuffer.allocateDirect(4*arrColT.length);
		vertexBuf.order(ByteOrder.nativeOrder());
		bufColT=vertexBuf.asFloatBuffer();
		bufColT.put(arrColT);
		vertexBuf = ByteBuffer.allocateDirect(2*arrDrawT.length);
		vertexBuf.order(ByteOrder.nativeOrder());
		bufDrawT=vertexBuf.asShortBuffer();
		bufDrawT.put(arrDrawT);
		vertexBuf = ByteBuffer.allocateDirect(4*arrNormT.length);
		vertexBuf.order(ByteOrder.nativeOrder());
		bufNormT=vertexBuf.asFloatBuffer();
		bufNormT.put(arrNormT);
        
		bufVerT.position(0);
		bufColT.position(0);
		bufNormT.position(0);
		bufDrawT.position(0);
	}
	public void setMatrix(int i){
		switch(i){
		case(0):
			currMatrix = cfWorld;
		break;
		case(1):
			currMatrix = cfSquare;
		break;
		case(2):
			currMatrix = cfPyramid;
		break;
		}
	}
	
	public void Click(int i){
		switch(i){
		case 0://for
			Matrix.translateM(currMatrix, 0, 0, 0, .2f);
			break;
		case 1://bak
			Matrix.translateM(currMatrix, 0, 0, 0, -.2f);
			break;
		case 2://left
			Matrix.translateM(currMatrix, 0, -.2f, 0, 0);
			break;
		case 3://rig
			Matrix.translateM(currMatrix, 0, .2f, 0, 0);
			break;
		case 4://tilkt
			gl.glPushMatrix();
			Matrix.rotateM(currMatrix, 0, 18f,1, 0, 0);
			gl.glPopMatrix();
			break;
		case 5://turnright
			gl.glPushMatrix();
			Matrix.rotateM(currMatrix, 0, 18f, 0, 1, 0);
			gl.glPopMatrix();
			break;
		case 6://Roll
			gl.glPushMatrix();
			Matrix.rotateM(currMatrix, 0, 18f, 0, 0, 1);
			gl.glPopMatrix();
			break;
		case 7://Set World
			currMatrix = cfWorld;
			break;
		case 8://Set Square
			currMatrix = cfSquare;
			break;
		case 9://Set Pyramid
			currMatrix = cfPyramid;
			break;
		case 10://down
			Matrix.translateM(currMatrix, 0, 0, -.2f, 0);
			break;
		case 11:
			Matrix.translateM(currMatrix, 0, 0, .2f, 0);
			break;
		case 12:
			currMatrix = cfLight;
			break;
		case 14:
			isAnimating = !isAnimating;
		}
	}
}

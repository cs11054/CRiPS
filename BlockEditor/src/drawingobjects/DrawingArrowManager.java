package drawingobjects;

import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import renderable.RenderableBlock;
import renderable.ScopeChecker;
import workspace.Workspace;
import workspace.WorkspaceEvent;
import workspace.WorkspaceListener;
import codeblocks.Block;
import codeblocks.BlockConnector;
import codeblocks.BlockStub;
import controller.WorkspaceController;

public class DrawingArrowManager implements WorkspaceListener {

	private static Map<Long, ArrowObject> arrows = new HashMap<Long, ArrowObject>();
	private static boolean isActive = true;
	public static int ARROW_GAP = 15;
	
	
	public static void addPossesser(Long id, ArrowObject arrow) {
		arrows.put(id, arrow);
	}

	public static void removePossesser(Long id) {
		arrows.remove(id);
	}

	public static void setActive(boolean isActive) {
		DrawingArrowManager.isActive = isActive;
		for(Long id : arrows.keySet()){
			arrows.get(id).setVisible(isActive);
		}
	}

	public static boolean isActive() {
		return isActive;
	}

	public static void clearPossessers() {
		arrows.clear();
	}

	public static void updatePossessers() {

	}

	public static void clearPosesser(ArrowObject arrow) {
		arrows.remove(arrow);
	}

	public static void setVisible(boolean active) {
		if (active) {
			Workspace.getInstance().getWorkSpaceController().showAllTraceLine();
		} else {
			Workspace.getInstance().getWorkSpaceController().disposeTraceLine();
		}
	}

	public static void resetArrowsPosition() {
//		for (RenderableBlock rb : arrows) {
//			rb.resetArrowPosition();
//		}
	}

	public static void thinArrows(RenderableBlock rBlock) {
//		if(rBlock != null){
//			boolean isThin = calcConcentration(rBlock);
//			if (rBlock.getEndArrows().size() > 0) {
//				Point p = rBlock.getLocation();
//				p.x += rBlock.getWidth();
//				p.y += rBlock.getHeight() / 2;
//
//				for (ArrowObject endArrow : rBlock.getEndArrows()) {
//					endArrow.setStartPoint(p);
//					endArrow.chengeColor(isThin);
//				}
//			}	
//		}
	}

	public static boolean calcConcentration(RenderableBlock rBlock) {
		boolean isThin = false;

		//stub
		if (rBlock.getBlock() instanceof BlockStub) {
			//引数がない
			if (hasEmptySocket(rBlock.getBlock())) {
				isThin = true;
			}
			{//孤島かどうか
				while (rBlock != null && rBlock.getBlock().getPlug()!= null) {
					rBlock = RenderableBlock.getRenderableBlock(rBlock.getBlock().getPlugBlockID());
				}
				//rblock == null は独立した引数ブロック 
				if (rBlock == null || ScopeChecker.isIndependentBlock(rBlock.getBlock())) {
					isThin = true;
				}
			}
		}
		return isThin;
	}

	public static boolean hasEmptySocket(Block block) {
		for (Iterator<BlockConnector> itarator = block.getSockets().iterator(); itarator
				.hasNext();) {
			if (!itarator.next().hasBlock()) {
				return true;
			}
		}
		return false;
	}

	public static Point calcCallerBlockPoint(RenderableBlock callerblock) {
		Point p1 = callerblock.getLocation();
		p1.x += callerblock.getWidth();
		p1.y += callerblock.getHeight() / 2;

		return p1;
	}

	public static Point calcDefinisionBlockPoint(RenderableBlock parentBlock) {
		Point p2 = new Point(parentBlock.getLocation());
		p2.y += parentBlock.getHeight() / 2;

		return p2;
	}

	public static void removeArrows(RenderableBlock block) {
		Workspace ws = Workspace.getInstance();
		WorkspaceController wc = ws.getWorkSpaceController();
		removeArrow(block, wc, ws);
		wc.getWorkspace().getPageNamed(wc.calcClassName()).getJComponent().repaint();
	}

	public static void removeArrow(RenderableBlock block, WorkspaceController wc, Workspace ws) {
//		if (block != null) {
//			for (ArrowObject arrow : block.getEndArrows()) {
//				ws.getPageNamed(wc.calcClassName()).clearArrow((Object) arrow);
//			}
//			for (ArrowObject arrow : block.getStartArrows()) {
//				ws.getPageNamed(wc.calcClassName()).clearArrow((Object) arrow);
//			}
//
//			DrawingArrowManager.clearPosesser(block);
//
//			Iterable<BlockConnector> sockets = block.getBlock().getSockets();
//			if (sockets != null) {
//				Iterator<BlockConnector> socketConnectors = sockets.iterator();
//				while (socketConnectors.hasNext()) {
//					removeArrow(RenderableBlock.getRenderableBlock(socketConnectors.next().getBlockID()), wc, ws);
//				}
//			}
//
//			if (hasNoAfterBlock(block.getBlock())) {
//				removeArrow(RenderableBlock.getRenderableBlock(block.getBlock().getAfterBlockID()), wc, ws);
//			}
//		}
	}

	public static boolean hasNoAfterBlock(Block block){
		if(block != null){
			if (block.getAfterBlockID() != -1 || block.getAfterBlockID() != null) {
				return true;
			}	
		}
		return false;
	}
	
	public static boolean isRecursiveFunction(Block topBlock, Block callerBlock){
		if(callerBlock instanceof BlockStub && topBlock.getBlockID().equals(((BlockStub)callerBlock).getParent().getBlockID())){
			return true;
		}
		return false;
	}
	
	public void workspaceEventOccurred(WorkspaceEvent event) {
		System.out.println(event.getEventType());
		if(event.getEventType() == WorkspaceEvent.CALLERBLOCK_CREATED){
			//callerかつ表示状態ならcalleeとの矢印を作成
			RenderableBlock sourceBlock = RenderableBlock.getRenderableBlock(event.getSourceBlockID());
			System.out.println(sourceBlock.getBlock().getGenusName());
			if(sourceBlock.getBlock().getGenusName().equals("callerprocedure")){
				//呼び出し元を取ってくる
				RenderableBlock calleeBlock = RenderableBlock.getRenderableBlock(((BlockStub)sourceBlock.getBlock()).getParent().getBlockID());
				ArrowObject arrow = new ArrowObject(sourceBlock, calleeBlock, isActive);
				arrows.put(sourceBlock.getBlockID(), arrow);
				Workspace.getInstance().getPageNamed(Workspace.getInstance().getWorkSpaceController().calcClassName()).addArrow(arrow);
			}
		}
		if(event.getEventType() == WorkspaceEvent.BLOCK_COLLAPSED){
			System.out.println(RenderableBlock.getRenderableBlock(event.getSourceBlockID()).toString());
		}
		
//		if (event.getEventType() == WorkspaceEvent.BLOCKS_DISCONNECTED || event.getEventType() == WorkspaceEvent.BLOCKS_CONNECTED || event.getEventType() == WorkspaceEvent.BLOCK_MOVED || event.getEventType() == WorkspaceEvent.BLOCK_COLLAPSED) {
//			updatePossessers();
//		}
		
//		if (event.getEventType() == WorkspaceEvent.BLOCK_REMOVED) {
//			removePossesser(RenderableBlock.getRenderableBlock(event.getSourceBlockID()));
//		}
	}

}

package chess.engine.player;

public enum MoveStatus {
	DONE {
		@Override
		public boolean isDone() {
			// TODO Auto-generated method stub
			return true;
		}
	},
	ILLEGAL_MOVEMENT {
		@Override
		public boolean isDone() {
			// TODO Auto-generated method stub
			return false;
		}
	}, 
	LEAVES_PLAYER_CHECK {
		@Override
		public boolean isDone() {
			// TODO Auto-generated method stub
			return false;
		}
	};
	
	public abstract boolean isDone();
}
